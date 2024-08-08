import * as THREE from 'three';
import TWEEN from '@tweenjs/tween.js';
import * as CANNON from 'cannon';
import eventBus from '@/common/lib/eventBus.js';

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

  updateCameraAndWalls() {
    const aspect = this.container.clientWidth / this.container.clientHeight;
    const zoom = 1; // 줌 레벨을 적절히 조정
    this.camera.position.set(0, 10, 0); // y축 위치를 조정하여 주사위가 굴려지는 바닥에 더 가깝게 설정
    this.camera.lookAt(0, 0, 0);
    this.camera.zoom = zoom;
    this.camera.updateProjectionMatrix();
  
    this.wallSize = {
      width: 20 * aspect,
      height: 10,
      depth: 20
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
    const floorMaterial = new THREE.MeshBasicMaterial({ color: 0x108080, side: THREE.DoubleSide });
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
      { x: 0, y: -1, z: 0, rotation: { x: -Math.PI / 2, y: 0, z: 0 } }, // 바닥 벽 추가
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
    const wallMaterial = new THREE.MeshBasicMaterial({ color: 0xFF0000, side: THREE.DoubleSide, opacity: 0.5, transparent: true });
  
    const leftWall = new THREE.Mesh(wallGeometry, wallMaterial);
    leftWall.rotation.y = Math.PI / 2;
    leftWall.position.set(-halfWidth, wallHeight / 2, 0);
    this.scene.add(leftWall);
  
    const rightWall = new THREE.Mesh(wallGeometry, wallMaterial);
    rightWall.rotation.y = -Math.PI / 2;
    rightWall.position.set(halfWidth, wallHeight / 2, 0);
    this.scene.add(rightWall);
  
    const frontWall = new THREE.Mesh(new THREE.PlaneGeometry(this.wallSize.depth, wallHeight), wallMaterial);
    frontWall.rotation.y = 0;
    frontWall.position.set(0, wallHeight / 2, -halfDepth);
    this.scene.add(frontWall);
  
    const backWall = new THREE.Mesh(new THREE.PlaneGeometry(this.wallSize.depth, wallHeight), wallMaterial);
    backWall.rotation.y = Math.PI;
    backWall.position.set(0, wallHeight / 2 -3, halfDepth);
    this.scene.add(backWall);
  }
  
  
  createD10Geometry() {
    const geometry = new THREE.BufferGeometry();
    const vertices = [];
    const indices = [];
  
    // 상단 뿔의 꼭지점
    vertices.push(0, 1, 0);
  
    // 상단 뿔의 기저 꼭지점들
    for (let i = 0; i < 5; i++) {
      const angle = (i * Math.PI * 2) / 5;
      const x = Math.cos(angle);
      const y = 0;
      const z = Math.sin(angle);
      vertices.push(x, y, z);
    }
  
    // 하단 뿔의 꼭지점
    vertices.push(0, -1, 0);
  
    // 상단 뿔의 면 (반시계 방향으로 수정)
    for (let i = 1; i <= 5; i++) {
      indices.push(0, i % 5 + 1, i);
    }
  
    // 하단 뿔의 면 (반시계 방향으로 수정)
    for (let i = 1; i <= 5; i++) {
      indices.push(6, i, i % 5 + 1);
    }
  
    geometry.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3));
    geometry.setIndex(indices);
    geometry.computeVertexNormals();
  
    return geometry;
  }
  
  createConvexPolyhedron(geometry) {
    if (!geometry.index) {
      console.warn('Geometry index is missing. Generating index.');
      geometry = this.createIndexedBufferGeometry(geometry);
    }
    if (!geometry.attributes.position) {
      throw new Error('Geometry position attribute is missing.');
    }
  
    const vertices = Array.from(geometry.attributes.position.array);
    const indices = Array.from(geometry.index.array);
    const cannonVertices = [];
    for (let i = 0; i < vertices.length; i += 3) {
      cannonVertices.push(new CANNON.Vec3(vertices[i], vertices[i + 1], vertices[i + 2]));
    }
    const cannonFaces = [];
    for (let i = 0; i < indices.length; i += 3) {
      cannonFaces.push([indices[i], indices[i + 1], indices[i + 2]]);
    }
  
    return new CANNON.ConvexPolyhedron(cannonVertices, cannonFaces);
  }
  

  createIndexedBufferGeometry(geometry) {
    if (geometry.index) {
      console.warn('THREE.BufferGeometry.toNonIndexed(): BufferGeometry is already non-indexed.');
      return geometry;
    }
  
    const nonIndexed = geometry.toNonIndexed();
    const position = nonIndexed.attributes.position;
    const indices = [];
  
    for (let i = 0; i < position.count; i++) {
      indices.push(i);
    }
  
    nonIndexed.setIndex(indices);
    return nonIndexed;
  }
  loadTexture(url) {
    return new Promise((resolve, reject) => {
      new THREE.TextureLoader().load(url, resolve, undefined, reject);
    });
  }
  

  async loadD4Textures() {
    const textureUrls = [
      '@/assets/images/ingame/123.png',
      '@/assets/images/ingame/124.png',
      '@/assets/images/ingame/134.png',
      '@/assets/images/ingame/234.png',
    ];
    return Promise.all(textureUrls.map(url => this.loadTexture(url)));
  }

  createDice(type) {
    let geometry;
    let shape;
    let diceMesh;
    switch (type) {
      case 4:
        geometry = new THREE.TetrahedronGeometry(1);
        shape = this.createConvexPolyhedron(geometry);
        
        break;
      case 6:
        geometry = new THREE.BoxGeometry(1, 1, 1);
        shape = new CANNON.Box(new CANNON.Vec3(0.5, 0.5, 0.5));
        break;
      case 8:
        geometry = new THREE.OctahedronGeometry(1);
        shape = this.createConvexPolyhedron(geometry);
        break;
      case 10:
        geometry = this.createD10Geometry();
        shape = this.createConvexPolyhedron(geometry);
        break;
      case 12:
        geometry = new THREE.DodecahedronGeometry(1);
        shape = this.createConvexPolyhedron(geometry);
        break;
      case 20:
        geometry = new THREE.IcosahedronGeometry(1);
        shape = this.createConvexPolyhedron(geometry);
        break;
      default:
        geometry = new THREE.BoxGeometry(1, 1, 1);
        shape = new CANNON.Box(new CANNON.Vec3(0.5, 0.5, 0.5));
    }
    const material = new THREE.MeshBasicMaterial({ color: 0x804000, side: THREE.DoubleSide });
    diceMesh = new THREE.Mesh(geometry, material);

    const edges = new THREE.EdgesGeometry(geometry);
    const lineMaterial = new THREE.LineBasicMaterial({ color: 0x000000 });
    const lineSegments = new THREE.LineSegments(edges, lineMaterial);
    diceMesh.add(lineSegments);
  
    const mass = 1;
    const body = new CANNON.Body({ mass });
    body.addShape(shape);
  
    body.quaternion.set(0, 0, 0, 1);
  
    return { diceMesh, body };
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
    console.log(`Applying force: ${force.x}, ${force.y}, ${force.z}`);
    body.applyImpulse(force, new CANNON.Vec3(0, 0, 0));
  
    // 주사위에 회전 속도를 설정
    const angularVelocity = new CANNON.Vec3(
      Math.random() * 10 - 5,
      Math.random() * 10 - 5,
      Math.random() * 10 - 5
    );
    console.log(`Applying angular velocity: ${angularVelocity.x}, ${angularVelocity.y}, ${angularVelocity.z}`);
    body.angularVelocity.set(angularVelocity.x, angularVelocity.y, angularVelocity.z);
  
    return new Promise(resolve => {
      setTimeout(() => {
        const result = Math.floor((Math.abs(body.quaternion.x) + Math.abs(body.quaternion.y) + Math.abs(body.quaternion.z)) % type) + 1;
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
      if (dice.material) dice.material.dispose();
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
    this.updateCameraAndWalls(); // 창 크기 변경 시 카메라와 벽 업데이트
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