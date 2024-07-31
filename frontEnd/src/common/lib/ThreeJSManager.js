import * as THREE from 'three';
import TWEEN from '@tweenjs/tween.js';
import * as CANNON from 'cannon';
import eventBus from '@/common/lib/eventBus.js';
import { TextGeometry } from 'three/examples/jsm/geometries/TextGeometry.js';
import { FontLoader } from 'three/examples/jsm/loaders/FontLoader.js';
class ThreeJSManager {
constructor(container) {
  this.container = container;
  this.scene = new THREE.Scene();
  this.camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
  this.updateCameraAndWalls(); // 초기 설정 시 카메라와 벽 업데이트
  this.renderer = new THREE.WebGLRenderer({ alpha: true });
  this.renderer.setSize(container.clientWidth, container.clientHeight);
  container.appendChild(this.renderer.domElement);
  const light = new THREE.AmbientLight(0x404040);
  this.scene.add(light);
  this.diceMeshes = [];
  this.diceBodies = [];
  this.world = new CANNON.World();
  this.world.gravity.set(0, -15, 0);
  this.world.broadphase = new CANNON.NaiveBroadphase();
  this.world.solver.iterations = 10;
  this.world.solver.tolerance = 0.1;
  this.diceMaterial = new CANNON.Material('diceMaterial');
  this.floorMaterial = new CANNON.Material('floorMaterial');
  this.contactMaterial = new CANNON.ContactMaterial(this.diceMaterial, this.floorMaterial, {
    friction: 0.4,
    restitution: 0.6,
  });
  this.world.addContactMaterial(this.contactMaterial);
  
  this.addFloor();
  this.addWalls();
  this.animate = this.animate.bind(this);
  this.animate();

  eventBus.on('change-camera', this.changeCameraPosition.bind(this));
  eventBus.on('roll-dice', this.rollDice.bind(this)); // roll-dice 이벤트 구독
  window.addEventListener('resize', this.onWindowResize.bind(this));
  }
  loadFont() {
    const loader = new FontLoader();
    return new Promise((resolve, reject) => {
      loader.load('@/assets/fonts/Roboto_Regular.json', (font) => {
        this.font = font;
        resolve(font);
      }, undefined, reject);
    });
  }
  createTextGeometry(text, size = 0.2, height = 0.05) {
    if (!this.font) {
      console.error('Font not loaded');
      return null;
    }
    return new TextGeometry(text, {
      font: this.font,
      size: size,
      height: height
    });
  }
  addNumbersToFaces(diceMesh, type) {
    const faceOrder = {
      4: [1, 2, 3, 4],
      6: [1, 6, 2, 5, 3, 4],
      8: [1, 2, 3, 4, 5, 6, 7, 8],
      10: [1, 2, 3, 4, 5, 6, 7, 8, 9, 0],
      12: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
      20: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
    };
  
    const order = faceOrder[type] || [];
  
    for (let i = 0; i < type; i++) {
      const number = order[i].toString();
      const planeGeometry = new THREE.PlaneGeometry(0.2, 0.2);
      const canvas = document.createElement('canvas');
      canvas.width = 64;
      canvas.height = 64;
      const context = canvas.getContext('2d');
      context.fillStyle = 'white';
      context.fillRect(0, 0, 64, 64);
      context.font = 'bold 48px Arial';
      context.fillStyle = 'black';
      context.textAlign = 'center';
      context.textBaseline = 'middle';
      context.fillText(number, 32, 32);
      
      const texture = new THREE.CanvasTexture(canvas);
      const material = new THREE.MeshBasicMaterial({ map: texture, transparent: true, side: THREE.DoubleSide });
      const planeMesh = new THREE.Mesh(planeGeometry, material);
  
      // 주사위 면의 법선 벡터를 구합니다.
      const normal = diceMesh.geometry.getAttribute('normal');
      const normalVector = new THREE.Vector3(normal.getX(i * 3), normal.getY(i * 3), normal.getZ(i * 3));
      
      // 숫자 평면을 주사위 면에 정렬합니다.
      planeMesh.lookAt(normalVector);
      
      // 숫자 평면을 주사위 표면에 배치합니다.
      planeMesh.position.copy(normalVector.multiplyScalar(0.501));  // 약간 표면 위에 배치
  
      diceMesh.add(planeMesh);
    }
  }

  updateCameraAndWalls() {
    const aspect = this.container.clientWidth / this.container.clientHeight;
    const zoom = 1;
    this.camera.position.set(0, 10, 0);
    this.camera.lookAt(0, 0, 0);
    this.camera.zoom = zoom;
    this.camera.updateProjectionMatrix();

    this.wallSize = {
      width: 20 * aspect,
      height: 10,
      depth: 20,
    };
  }

  addFloor() {
    const floorShape = new CANNON.Plane();
    const floorBody = new CANNON.Body({ mass: 0 });
    floorBody.addShape(floorShape);
    floorBody.quaternion.setFromEuler(-Math.PI / 2, 0, 0);
    floorBody.position.y = -5;
    this.world.addBody(floorBody);

    const floorGeometry = new THREE.PlaneGeometry(this.wallSize.width, this.wallSize.depth);
    const floorMaterial = new THREE.MeshBasicMaterial({ color: 0x108080, side: THREE.DoubleSide, opacity: 0, transparent: true  });
    const floorMesh = new THREE.Mesh(floorGeometry, floorMaterial);
    floorMesh.rotation.x = -Math.PI / 2;
    floorMesh.position.y = -5;
    this.scene.add(floorMesh);
  }

  addWalls() {
    const wallHeight = this.wallSize.height;
    const halfWidth = this.wallSize.width / 3;
    const halfDepth = this.wallSize.depth / 3.5;

    const walls = [
      { x: -halfWidth, y: wallHeight / 2, z: 0, rotation: { x: 0, y: Math.PI / 2, z: 0 } },
      { x: halfWidth, y: wallHeight / 2, z: 0, rotation: { x: 0, y: -Math.PI / 2, z: 0 } },
      { x: 0, y: wallHeight / 2, z: -halfDepth, rotation: { x: 0, y: 0, z: 0 } },
      { x: 0, y: wallHeight / 2, z: halfDepth, rotation: { x: 0, y: Math.PI, z: 0 } },
      { x: 0, y: wallHeight, z: 0, rotation: { x: Math.PI / 2, y: 0, z: 0 } },
      { x: 0, y: -1, z: 0, rotation: { x: -Math.PI / 2, y: 0, z: 0 } },
    ];

    walls.forEach(({ x, y, z, rotation }) => {
      const wallShape = new CANNON.Plane();
      const wallBody = new CANNON.Body({ mass: 0 });
      wallBody.addShape(wallShape);
      wallBody.position.set(x, y, z);
      wallBody.quaternion.setFromEuler(rotation.x, rotation.y, rotation.z);
      this.world.addBody(wallBody);
    });

    const wallGeometry = new THREE.PlaneGeometry(this.wallSize.width, wallHeight);
    const wallMaterial = new THREE.MeshBasicMaterial({ color: 0xFF0000, side: THREE.DoubleSide, opacity: 0, transparent: true });

    const leftWall = new THREE.Mesh(wallGeometry, wallMaterial);
    leftWall.rotation.y = Math.PI / 2;
    leftWall.position.set(-halfWidth, wallHeight / 2 + 1, 0);
    this.scene.add(leftWall);

    const rightWall = new THREE.Mesh(wallGeometry, wallMaterial);
    rightWall.rotation.y = -Math.PI / 2;
    rightWall.position.set(halfWidth, wallHeight / 2 + 1, 0);
    this.scene.add(rightWall);

    const frontWall = new THREE.Mesh(new THREE.PlaneGeometry(this.wallSize.depth, wallHeight), wallMaterial);
    frontWall.rotation.y = 0;
    frontWall.position.set(0, wallHeight / 2 + 2, -halfDepth);
    this.scene.add(frontWall);

    const backWall = new THREE.Mesh(new THREE.PlaneGeometry(this.wallSize.depth, wallHeight), wallMaterial);
    backWall.rotation.y = Math.PI;
    backWall.position.set(0, wallHeight / 2 - 3, halfDepth);
    this.scene.add(backWall);
  }

  createD10Geometry() {
    const geometry = new THREE.BufferGeometry();
    const vertices = [];
    const indices = [];

    vertices.push(0, 1, 0);

    for (let i = 0; i < 5; i++) {
      const angle = (i * Math.PI * 2) / 5;
      const x = Math.cos(angle);
      const y = 0;
      const z = Math.sin(angle);
      vertices.push(x, y, z);
    }

    vertices.push(0, -1, 0);

    for (let i = 1; i <= 5; i++) {
      indices.push(0, i % 5 + 1, i);
    }

    for (let i = 1; i <= 5; i++) {
      indices.push(6, i, i % 5 + 1);
    }

    geometry.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3));
    geometry.setIndex(indices);
    geometry.computeVertexNormals();

    return geometry;
  }

  createConvexPolyhedron(geometry) {
    const vertices = [];
    const faces = [];
  
    const positionAttribute = geometry.getAttribute('position');
    for (let i = 0; i < positionAttribute.count; i++) {
      vertices.push(new CANNON.Vec3().fromArray(positionAttribute.array, i * 3));
    }
  
    if (geometry.index) {
      for (let i = 0; i < geometry.index.count; i += 3) {
        faces.push([
          geometry.index.array[i],
          geometry.index.array[i + 1],
          geometry.index.array[i + 2]
        ]);
      }
    } else {
      for (let i = 0; i < positionAttribute.count; i += 3) {
        faces.push([i, i + 1, i + 2]);
      }
    }
  
    return new CANNON.ConvexPolyhedron({ vertices, faces });
  }

  // createIndexedBufferGeometry(geometry) {
  //   if (geometry.index) {
  //     console.warn('THREE.BufferGeometry.toNonIndexed(): BufferGeometry is already non-indexed.');
  //     return geometry;
  //   }
  
  //   const nonIndexed = geometry.toNonIndexed();
  //   const position = nonIndexed.attributes.position;
  //   const indices = [];
  
  //   for (let i = 0; i < position.count; i++) {
  //     indices.push(i);
  //   }
  
  //   nonIndexed.setIndex(indices);
  //   return nonIndexed;
  // }

  createDice(type) {
    let geometry;
    let shape;
    let diceMesh;
    const scale = 0.5;
  
    switch (type) {
      case 4:
        geometry = new THREE.TetrahedronGeometry(scale);
        break;
      case 6:
        geometry = new THREE.BoxGeometry(scale, scale, scale);
        break;
      case 8:
        geometry = new THREE.OctahedronGeometry(scale);
        break;
      case 10:
        geometry = this.createD10Geometry(scale);
        break;
      case 12:
        geometry = new THREE.DodecahedronGeometry(scale);
        break;
      case 20:
        geometry = new THREE.IcosahedronGeometry(scale);
        break;
      case 100:
        return this.createD100Dice(scale);
      default:
        throw new Error(`Unsupported dice type: ${type}`);
    }
  
    const material = new THREE.MeshPhongMaterial({ color: 0xffffff });
    diceMesh = new THREE.Mesh(geometry, material);
    
    this.addNumbersToFaces(diceMesh, type);
  
    shape = this.createConvexPolyhedron(geometry);
    
    const mass = 1;
    const body = new CANNON.Body({ mass });
    body.addShape(shape);
  
    return { diceMesh, body };
  }

  createD100Dice() {
    const d10Mesh1 = this.createDice(10).diceMesh;
    const d10Mesh2 = this.createDice(10).diceMesh;
    d10Mesh2.scale.setScalar(0.8);
    d10Mesh2.position.set(0.6, 0, 0);
  
    const geometry1 = d10Mesh1.geometry.clone();
    const geometry2 = d10Mesh2.geometry.clone();
    
    geometry2.scale(0.8, 0.8, 0.8);
    geometry2.translate(0.6, 0, 0);
    
    const mergedGeometry = BufferGeometryUtils.mergeBufferGeometries([geometry1, geometry2]);
  
    const material = new THREE.MeshPhongMaterial({ color: 0x804000 });
    const diceMesh = new THREE.Mesh(mergedGeometry, material);
  
    this.addNumbersToFaces(diceMesh, 100);  // D100 텍스처 추가
  
    const shape = this.createConvexPolyhedron(mergedGeometry);
    const mass = 1;
    const body = new CANNON.Body({ mass });
    body.addShape(shape);
  
    return { diceMesh, body };
  }

  async addNumbersToFaces(diceMesh, type) {
    if (!this.font) {
      await this.loadFont();
    }
  
    const faceOrder = {
      4: [1, 2, 3, 4],
      6: [1, 6, 2, 5, 3, 4],
      8: [1, 2, 3, 4, 5, 6, 7, 8],
      10: [1, 2, 3, 4, 5, 6, 7, 8, 9, 0],
      12: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
      20: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
    };
  
    const order = faceOrder[type] || [];
  
    for (let i = 0; i < type; i++) {
      const number = order[i].toString();
      const textGeometry = this.createTextGeometry(number);
      const textMaterial = new THREE.MeshPhongMaterial({ color: 0x000000 });
      const textMesh = new THREE.Mesh(textGeometry, textMaterial);
  
      // 주사위 면의 법선 벡터를 구합니다.
      const normal = diceMesh.geometry.getAttribute('normal');
      const normalVector = new THREE.Vector3(normal.getX(i * 3), normal.getY(i * 3), normal.getZ(i * 3));
      
      // 텍스트를 주사위 면에 정렬합니다.
      textMesh.lookAt(normalVector);
      
      // 텍스트를 주사위 표면에 배치합니다.
      textMesh.position.copy(normalVector.multiplyScalar(0.501));  // 약간 표면 위에 배치
  
      diceMesh.add(textMesh);
    }
  }

  getUppermostFace(body, type) {
    const rotation = body.quaternion;
  const rotationMatrix = new CANNON.Mat3();
    
  const x = rotation.x, y = rotation.y, z = rotation.z, w = rotation.w;
  const x2 = x + x, y2 = y + y, z2 = z + z;
  const xx = x * x2, xy = x * y2, xz = x * z2;
  const yy = y * y2, yz = y * z2, zz = z * z2;
  const wx = w * x2, wy = w * y2, wz = w * z2;

  rotationMatrix.elements[0] = 1 - (yy + zz);
  rotationMatrix.elements[1] = xy - wz;
  rotationMatrix.elements[2] = xz + wy;
  rotationMatrix.elements[3] = xy + wz;
  rotationMatrix.elements[4] = 1 - (xx + zz);
  rotationMatrix.elements[5] = yz - wx;
  rotationMatrix.elements[6] = xz - wy;
  rotationMatrix.elements[7] = yz + wx;
  rotationMatrix.elements[8] = 1 - (xx + yy);

    let faces;
    switch (type) {
      case 4:
        faces = this.getTetrahedronFaces();
        break;
      case 6:
        faces = this.getCubeFaces();
        break;
      case 8:
        faces = this.getOctahedronFaces();
        break;
      case 10:
        faces = this.getD10Faces();
        break;
      case 12:
        faces = this.getDodecahedronFaces();
        break;
      case 20:
        faces = this.getIcosahedronFaces();
        break;
      case 100:
        return this.getD100Result(body);
      default:
        throw new Error(`Unsupported dice type: ${type}`);
    }

    let maxDot = -Infinity;
    let uppermostFaceIndex = -1;

    faces.forEach((face, index) => {
    const rotatedFace = new CANNON.Vec3(
      rotationMatrix.elements[0] * face.x + rotationMatrix.elements[1] * face.y + rotationMatrix.elements[2] * face.z,
      rotationMatrix.elements[3] * face.x + rotationMatrix.elements[4] * face.y + rotationMatrix.elements[5] * face.z,
      rotationMatrix.elements[6] * face.x + rotationMatrix.elements[7] * face.y + rotationMatrix.elements[8] * face.z
    );
    const dot = rotatedFace.dot(new CANNON.Vec3(0, 1, 0));
    if ((type === 4 ? -dot : dot) > maxDot) {
      maxDot = (type === 4 ? -dot : dot);
      uppermostFaceIndex = index;
    }
  });

  return uppermostFaceIndex + 1;
  }

  getD100Result(body) {
    const tens = this.getUppermostFace(body, 10) - 1;
    const ones = this.getUppermostFace(body, 10) - 1;
    return tens * 10 + ones;
  }

  getTetrahedronFaces() {
    // 4면체의 면 벡터들 정의
    return [
      new CANNON.Vec3(0, 0.5, -0.866),
      new CANNON.Vec3(0.866, 0.5, 0),
      new CANNON.Vec3(-0.866, 0.5, 0),
      new CANNON.Vec3(0, -1, 0)
    ];
  }

  getCubeFaces() {
    // 6면체의 면 벡터들 정의
    return [
      new CANNON.Vec3(1, 0, 0),
      new CANNON.Vec3(-1, 0, 0),
      new CANNON.Vec3(0, 1, 0),
      new CANNON.Vec3(0, -1, 0),
      new CANNON.Vec3(0, 0, 1),
      new CANNON.Vec3(0, 0, -1)
    ];
  }

  getOctahedronFaces() {
    // 8면체의 면 벡터들 정의
    return [
      new CANNON.Vec3(1, 0, 0),
      new CANNON.Vec3(-1, 0, 0),
      new CANNON.Vec3(0, 1, 0),
      new CANNON.Vec3(0, -1, 0),
      new CANNON.Vec3(0, 0, 1),
      new CANNON.Vec3(0, 0, -1),
      new CANNON.Vec3(0.707, 0.707, 0),
      new CANNON.Vec3(-0.707, -0.707, 0)
    ];
  }

  getD10Faces() {
    // 10면체의 면 벡터들 정의
    const faces = [];
    for (let i = 0; i < 10; i++) {
      const angle = (i * Math.PI * 2) / 10;
      faces.push(new CANNON.Vec3(Math.cos(angle), 0.5, Math.sin(angle)));
    }
    return faces;
  }

  getDodecahedronFaces() {
    const phi = (1 + Math.sqrt(5)) / 2;
    const a = 1 / phi;
    const b = phi;
    return [
      new CANNON.Vec3(0, a, b), new CANNON.Vec3(0, -a, b),
      new CANNON.Vec3(0, a, -b), new CANNON.Vec3(0, -a, -b),
      new CANNON.Vec3(b, 0, a), new CANNON.Vec3(-b, 0, a),
      new CANNON.Vec3(b, 0, -a), new CANNON.Vec3(-b, 0, -a),
      new CANNON.Vec3(a, b, 0), new CANNON.Vec3(-a, b, 0),
      new CANNON.Vec3(a, -b, 0), new CANNON.Vec3(-a, -b, 0)
    ];
  }
  
  getIcosahedronFaces() {
    const phi = (1 + Math.sqrt(5)) / 2;
    return [
      new CANNON.Vec3(0, 1, phi), new CANNON.Vec3(0, -1, phi),
      new CANNON.Vec3(0, 1, -phi), new CANNON.Vec3(0, -1, -phi),
      new CANNON.Vec3(1, phi, 0), new CANNON.Vec3(-1, phi, 0),
      new CANNON.Vec3(1, -phi, 0), new CANNON.Vec3(-1, -phi, 0),
      new CANNON.Vec3(phi, 0, 1), new CANNON.Vec3(-phi, 0, 1),
      new CANNON.Vec3(phi, 0, -1), new CANNON.Vec3(-phi, 0, -1),
      new CANNON.Vec3(1, 1, 1), new CANNON.Vec3(-1, 1, 1),
      new CANNON.Vec3(1, -1, 1), new CANNON.Vec3(-1, -1, 1),
      new CANNON.Vec3(1, 1, -1), new CANNON.Vec3(-1, 1, -1),
      new CANNON.Vec3(1, -1, -1), new CANNON.Vec3(-1, -1, -1)
    ];
  }



  rollDice(types) {
    this.clearDice();

    const promises = types.map((dice, index) => {
      return this.rollSingleDice(dice.type, index, types.length);
    });

    return Promise.all(promises);
  }

  rollSingleDice(type, index, total) {
    const { diceMesh, body } = this.createDice(type);
  
    // 벽 안에서 생성
    const startX = (Math.random() * (this.wallSize.width - 4) - (this.wallSize.width / 2 - 2));
    const startY = 1 + Math.random() * 1;
    const startZ = (Math.random() * (this.wallSize.depth - 4) - (this.wallSize.depth / 2 - 2));
  
    diceMesh.position.set(startX, startY, startZ);
    body.position.set(startX, startY, startZ);
    this.scene.add(diceMesh);
    this.diceMeshes.push(diceMesh);
    this.diceBodies.push(body);
    this.world.addBody(body);
  
    // 주사위에 힘을 가함
    const maxForce = 100;
    const force = new CANNON.Vec3(
      (Math.random() - 0.5) * maxForce,
      (Math.random() - 0.5) * maxForce / 90,
      (Math.random() - 0.5) * maxForce
    );
    body.applyImpulse(force, new CANNON.Vec3(0, 0, 0));
  
    // 주사위에 회전 속도를 설정
    const angularVelocity = new CANNON.Vec3(
      Math.random() * 10 - 5,
      Math.random() * 10 - 5,
      Math.random() * 10 - 5
    );
    body.angularVelocity.set(angularVelocity.x, angularVelocity.y, angularVelocity.z);
  
    return new Promise(resolve => {
      setTimeout(() => {
        const result = this.getUppermostFace(body, type);
        this.scene.remove(diceMesh);
        this.world.removeBody(body);
        this.diceMeshes = this.diceMeshes.filter(d => d !== diceMesh);
        this.diceBodies = this.diceBodies.filter(b => b !== body);
        resolve({ type, value: result });
      }, 7000);
    });
  }
  
  

  clearDice() {
    this.diceMeshes.forEach(dice => {
      this.scene.remove(dice);
      if (dice.geometry) dice.geometry.dispose();
      if (Array.isArray(dice.material)) {
        dice.material.forEach(mat => mat.dispose());
      } else if (dice.material) {
        dice.material.dispose();
      }
    });
    this.diceMeshes = [];
    this.diceBodies.forEach(body => {
      this.world.removeBody(body);
    });
    this.diceBodies = [];
  }
  
  

  changeCameraPosition() {
    new TWEEN.Tween(this.camera.position)
      .to({ x: 0, y: 20, z: 0 }, 3000)
      .easing(TWEEN.Easing.Quadratic.Out)
      .start();

    new TWEEN.Tween(this.camera.rotation)
      .to({ x: -Math.PI / 2, y: 0, z: 0 }, 2000)
      .easing(TWEEN.Easing.Quadratic.Out)
      .start();

    setTimeout(() => {
      this.resetCameraPosition();
    }, 15000);
  }


  resetCameraPosition() {
    new TWEEN.Tween(this.camera.position)
      .to({ x: 0, y: 20, z: 0 }, 2000)
      .easing(TWEEN.Easing.Quadratic.Out)
      .start();

    new TWEEN.Tween(this.camera.rotation)
      .to({ x: -Math.PI / 2, y: 0, z: 0 }, 2000)
      .easing(TWEEN.Easing.Quadratic.Out)
      .start();
  }

  onWindowResize() {
    this.updateCameraAndWalls();
    this.renderer.setSize(this.container.clientWidth, this.container.clientHeight);
  }

  animate() {
    requestAnimationFrame(this.animate);
    TWEEN.update();
    try {
      this.world.step(1 / 60);
    } catch (error) {
      console.error("Physics simulation error:", error);
      // 필요한 경우 시뮬레이션 재설정 로직 추가
    }
    this.world.step(1 / 60);
    this.diceBodies.forEach((body, index) => {
      const diceMesh = this.diceMeshes[index];
      if (diceMesh && body) {
        diceMesh.position.copy(body.position);
        diceMesh.quaternion.copy(body.quaternion);
      }
    });
    this.renderer.render(this.scene, this.camera);
  }
}

export default ThreeJSManager;
