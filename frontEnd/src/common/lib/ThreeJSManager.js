import * as THREE from 'three'; // Three.js를 가져옴
import TWEEN from '@tweenjs/tween.js'; // Tween.js를 가져옴
import * as CANNON from 'cannon'; // Cannon.js를 가져옴
import eventBus from '@/common/lib/eventBus.js'; // 이벤트 버스를 가져옴
import { RoundedBoxGeometry } from 'three/examples/jsm/geometries/RoundedBoxGeometry.js';
import { BoxLineGeometry } from 'three/examples/jsm/geometries/BoxLineGeometry.js';

class ThreeJSManager {
  constructor(container) {
    this.container = container; // 컨테이너 엘리먼트를 저장
    this.scene = new THREE.Scene(); // Three.js 장면을 생성
    this.camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000); // 카메라 설정
    this.updateCameraAndWalls(); // 초기 설정 시 카메라와 벽 업데이트
    this.renderer = new THREE.WebGLRenderer({ alpha: true }); // 렌더러 생성
    this.renderer.setSize(container.clientWidth, container.clientHeight); // 렌더러 크기 설정
    container.appendChild(this.renderer.domElement); // 렌더러 DOM 엘리먼트를 컨테이너에 추가
    this.clock = new THREE.Clock(); // 시계 생성
    const ambientLight = new THREE.AmbientLight(0xffffff, 0.5);
    this.scene.add(ambientLight);
    const directionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
    directionalLight.position.set(5, 10, 7);
    this.scene.add(directionalLight);
    this.diceMeshes = []; // 주사위 메쉬 배열 초기화
    this.diceBodies = []; // 주사위 물리 바디 배열 초기화
    this.world = new CANNON.World(); // 물리 세계 생성
    this.world.gravity.set(0, -15, 0); // 중력 설정
    this.world.broadphase = new CANNON.NaiveBroadphase(); // 브로드페이즈 설정
    this.world.solver.iterations = 10; // 솔버 반복 설정
    this.world.solver.tolerance = 0.1; // 솔버 허용오차 설정
    this.diceMaterial = new CANNON.Material('diceMaterial'); // 주사위 재질 생성
    this.floorMaterial = new CANNON.Material('floorMaterial'); // 바닥 재질 생성
    this.contactMaterial = new CANNON.ContactMaterial(this.diceMaterial, this.floorMaterial, { // 접촉 재질 생성
      friction: 0.4,
      restitution: 0.6,
    });
    this.world.addContactMaterial(this.contactMaterial); // 접촉 재질을 물리 세계에 추가

    this.addFloor(); // 바닥 추가
    this.addWalls(); // 벽 추가
    this.animate = this.animate.bind(this); // animate 메서드를 this에 바인딩
    this.animate(); // 애니메이션 시작

    eventBus.on('roll-dice', this.rollDice.bind(this)); // roll-dice 이벤트 구독
    window.addEventListener('resize', this.onWindowResize.bind(this)); // 창 크기 조정 이벤트 핸들러 추가
  }

  updateCameraAndWalls() {
    const aspect = this.container.clientWidth / this.container.clientHeight; // 종횡비 계산
    const zoom = 1; // 줌 레벨 설정
    this.camera.position.set(0, 10, 0); // 카메라 위치 설정
    this.camera.lookAt(0, 0, 0); // 카메라가 원점을 바라보게 설정
    this.camera.zoom = zoom; // 카메라 줌 설정
    this.camera.updateProjectionMatrix(); // 카메라 투영 행렬 업데이트

    this.wallSize = { // 벽 크기 설정
      width: 20 * aspect,
      height: 10,
      depth: 20,
    };
  }

  addFloor() {
    const floorShape = new CANNON.Plane(); // 바닥 모양 생성
    const floorBody = new CANNON.Body({ mass: 0 }); // 바닥 물리 바디 생성
    floorBody.addShape(floorShape); // 바닥 모양을 바디에 추가
    floorBody.quaternion.setFromEuler(-Math.PI / 2, 0, 0); // 바닥 회전 설정
    floorBody.position.y = -5; // 바닥 위치 설정
    this.world.addBody(floorBody); // 바닥 바디를 물리 세계에 추가

    const floorGeometry = new THREE.PlaneGeometry(this.wallSize.width, this.wallSize.depth); // 바닥 지오메트리 생성
    const floorMaterial = new THREE.MeshBasicMaterial({ color: 0x108080, side: THREE.DoubleSide, opacity: 0, transparent: true }); // 바닥 재질 생성
    const floorMesh = new THREE.Mesh(floorGeometry, floorMaterial); // 바닥 메쉬 생성
    floorMesh.rotation.x = -Math.PI / 2; // 바닥 메쉬 회전 설정
    floorMesh.position.y = -5; // 바닥 메쉬 위치 설정
    this.scene.add(floorMesh); // 바닥 메쉬를 장면에 추가
  }

  addWalls() {
    const wallHeight = this.wallSize.height; // 벽 높이 설정
    const halfWidth = this.wallSize.width / 3; // 벽 너비의 절반 계산
    const halfDepth = this.wallSize.depth / 3.5; // 벽 깊이의 절반 계산

    const walls = [ // 벽 설정 배열
      { x: -halfWidth, y: wallHeight / 2, z: 0, rotation: { x: 0, y: Math.PI / 2, z: 0 } },
      { x: halfWidth, y: wallHeight / 2, z: 0, rotation: { x: 0, y: -Math.PI / 2, z: 0 } },
      { x: 0, y: wallHeight / 2, z: -halfDepth, rotation: { x: 0, y: 0, z: 0 } },
      { x: 0, y: wallHeight / 2, z: halfDepth, rotation: { x: 0, y: Math.PI, z: 0 } },
      { x: 0, y: wallHeight, z: 0, rotation: { x: Math.PI / 2, y: 0, z: 0 } },
      { x: 0, y: -1, z: 0, rotation: { x: -Math.PI / 2, y: 0, z: 0 } },
    ];

    walls.forEach(({ x, y, z, rotation }) => {
      const wallShape = new CANNON.Plane(); // 벽 모양 생성
      const wallBody = new CANNON.Body({ mass: 0 }); // 벽 물리 바디 생성
      wallBody.addShape(wallShape); // 벽 모양을 바디에 추가
      wallBody.position.set(x, y, z); // 벽 위치 설정
      wallBody.quaternion.setFromEuler(rotation.x, rotation.y, rotation.z); // 벽 회전 설정
      this.world.addBody(wallBody); // 벽 바디를 물리 세계에 추가
    });

    const wallGeometry = new THREE.PlaneGeometry(this.wallSize.width, wallHeight); // 벽 지오메트리 생성
    const wallMaterial = new THREE.MeshBasicMaterial({ color: 0xFF0000, side: THREE.DoubleSide, opacity: 0, transparent: true }); // 벽 재질 생성

    const leftWall = new THREE.Mesh(wallGeometry, wallMaterial); // 왼쪽 벽 메쉬 생성
    leftWall.rotation.y = Math.PI / 2; // 왼쪽 벽 회전 설정
    leftWall.position.set(-halfWidth, wallHeight / 2 + 1, 0); // 왼쪽 벽 위치 설정
    this.scene.add(leftWall); // 왼쪽 벽을 장면에 추가

    const rightWall = new THREE.Mesh(wallGeometry, wallMaterial); // 오른쪽 벽 메쉬 생성
    rightWall.rotation.y = -Math.PI / 2; // 오른쪽 벽 회전 설정
    rightWall.position.set(halfWidth, wallHeight / 2 + 1, 0); // 오른쪽 벽 위치 설정
    this.scene.add(rightWall); // 오른쪽 벽을 장면에 추가

    const frontWall = new THREE.Mesh(new THREE.PlaneGeometry(this.wallSize.depth, wallHeight), wallMaterial); // 앞쪽 벽 메쉬 생성
    frontWall.rotation.y = 0; // 앞쪽 벽 회전 설정
    frontWall.position.set(0, wallHeight / 2 + 2, -halfDepth); // 앞쪽 벽 위치 설정
    this.scene.add(frontWall); // 앞쪽 벽을 장면에 추가

    const backWall = new THREE.Mesh(new THREE.PlaneGeometry(this.wallSize.depth, wallHeight), wallMaterial); // 뒤쪽 벽 메쉬 생성
    backWall.rotation.y = Math.PI; // 뒤쪽 벽 회전 설정
    backWall.position.set(0, wallHeight / 2 - 3, halfDepth); // 뒤쪽 벽 위치 설정
    this.scene.add(backWall); // 뒤쪽 벽을 장면에 추가
  }

  createD10Geometry() {
    const geometry = new THREE.BufferGeometry(); // 지오메트리 생성
    const vertices = []; // 꼭짓점 배열 초기화
    const indices = []; // 인덱스 배열 초기화

    vertices.push(0, 1, 0); // 꼭짓점 추가

    for (let i = 0; i < 5; i++) {
      const angle = (i * Math.PI * 2) / 5; // 각도 계산
      const x = Math.cos(angle); // x 좌표 계산
      const y = 0; // y 좌표 설정
      const z = Math.sin(angle); // z 좌표 계산
      vertices.push(x, y, z); // 꼭짓점 추가
    }

    vertices.push(0, -1, 0); // 꼭짓점 추가

    for (let i = 1; i <= 5; i++) {
      indices.push(0, i % 5 + 1, i); // 인덱스 추가
    }

    for (let i = 1; i <= 5; i++) {
      indices.push(6, i, i % 5 + 1); // 인덱스 추가
    }

    geometry.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3)); // 지오메트리에 꼭짓점 속성 추가
    geometry.setIndex(indices); // 지오메트리에 인덱스 속성 추가
    geometry.computeVertexNormals(); // 꼭짓점 법선 계산

    return geometry; // 지오메트리 반환
  }

  createIndexedBufferGeometry(geometry) {
    if (geometry.index) {
      return geometry; // 이미 indexed면 그대로 반환
    }
  
    const position = geometry.attributes.position;
    const vertices = [];
    const indices = [];
    
    for (let i = 0; i < position.count; i++) {
      vertices.push(
        position.getX(i),
        position.getY(i),
        position.getZ(i)
      );
      indices.push(i);
    }
  
    const newGeometry = new THREE.BufferGeometry();
    newGeometry.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3));
    newGeometry.setIndex(indices);
    return newGeometry;
  }

  createConvexPolyhedron(geometry) {
    if (!geometry.index) {
      geometry = this.createIndexedBufferGeometry(geometry);
    }
  
    const positionAttribute = geometry.attributes.position;
    const index = geometry.index;
  
    const vertices = [];
    for (let i = 0; i < positionAttribute.count; i++) {
      vertices.push(new CANNON.Vec3(
        positionAttribute.getX(i),
        positionAttribute.getY(i),
        positionAttribute.getZ(i)
      ));
    }
  
    const faces = [];
    for (let i = 0; i < index.count; i += 3) {
      faces.push([index.getX(i), index.getX(i+1), index.getX(i+2)]);
    }
  
    // 여기서 새로운 ConvexPolyhedron을 생성하여 반환합니다
    return new CANNON.ConvexPolyhedron(vertices, faces);
  }

  createDice(type) {
    let geometry;
    let shape;
    let diceMesh;
    const scale = 1;
    let faceValues;
  
    switch (type) {
      case 4:
        geometry = new THREE.TetrahedronGeometry(scale);
        faceValues = [1, 2, 3, 4];
        break;
      case 6:
        geometry = new THREE.BoxGeometry(scale, scale, scale);
        faceValues = [2, 5, 1, 6, 3, 4];
        break;
      case 8:
        geometry = new THREE.OctahedronGeometry(scale);
        faceValues = [1, 2, 3, 4, 5, 6, 7, 8];
        break;
      case 10:
        geometry = this.createD10Geometry(scale);
        faceValues = [1, 2, 3, 4, 5, 6, 7, 8, 9, 0];
        break;
      case 12:
        geometry = new THREE.DodecahedronGeometry(scale);
        faceValues = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        break;
      case 20:
        geometry = new THREE.IcosahedronGeometry(scale);
        faceValues = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20];
        break;
      case 100:
        return this.createD100Dice(scale);
      default:
        throw new Error(`Unsupported dice type: ${type}`);
    }

    const materials = this.loadTextures(type);

    diceMesh = new THREE.Mesh(geometry, materials);
    diceMesh.userData = { faceValues };

    shape = this.createConvexPolyhedron(geometry);
    const mass = 1;
    const body = new CANNON.Body({ mass, shape });
    return { diceMesh, body };
  }

  createD100Dice(scale) {
    const geometry1 = this.createD10Geometry(scale);
    const geometry2 = this.createD10Geometry(scale);

    const material1 = new THREE.MeshPhongMaterial({ 
      color: 0xFFB347,  // 주황색
      shininess: 30,
      specular: 0x111111
    });

    const material2 = new THREE.MeshPhongMaterial({ 
      color: 0x87CEEB,  // 하늘색
      shininess: 30,
      specular: 0x111111
    });

    const diceMesh1 = new THREE.Mesh(geometry1, material1);
    const diceMesh2 = new THREE.Mesh(geometry2, material2);

    const edges1 = new THREE.EdgesGeometry(geometry1);
    const line1 = new THREE.LineSegments(edges1, new THREE.LineBasicMaterial({ color: 0x000000 }));
    diceMesh1.add(line1);

    const edges2 = new THREE.EdgesGeometry(geometry2);
    const line2 = new THREE.LineSegments(edges2, new THREE.LineBasicMaterial({ color: 0x000000 }));
    diceMesh2.add(line2);

    diceMesh2.position.set(1.2, 0, 0);

    const shape1 = this.createConvexPolyhedron(geometry1);
    const shape2 = this.createConvexPolyhedron(geometry2);

    const mass = 1;
    const body1 = new CANNON.Body({ mass, shape: shape1 });
    const body2 = new CANNON.Body({ mass, shape: shape2 });

    const group = new THREE.Group();
    group.add(diceMesh1);
    group.add(diceMesh2);

    return { 
      diceMesh: group, 
      body1: body1, 
      body2: body2 
    };
  }

  loadTextures(type) {
    const loader = new THREE.TextureLoader();
    const textures = [];
    const faceOrder = {
      4: [1, 2, 3, 4],
      6: [1, 6, 2, 5, 3, 4],
      8: [1, 2, 3, 4, 5, 6, 7, 8],
      10: [1, 2, 3, 4, 5, 6, 7, 8, 9, 0],
      12: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
      20: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20],
    };

    const order = faceOrder[type] || [];
    for (let i = 0; i < order.length; i++) {
      textures.push(loader.load(`/assets/dice/dd${order[i]}.png`));
    }

    return textures.map(texture => new THREE.MeshBasicMaterial({ map: texture }));
  }

  rollDice(types) {
    this.clearDice(); // 기존 주사위 제거

    const promises = types.map((dice) => {
      return this.rollSingleDice(dice.type); // 각 주사위 타입에 대해 주사위 굴리기
    });

    return Promise.all(promises); // 모든 주사위 결과를 반환
  }

  rollSingleDice(type) {
    let diceMesh, body, body2;
  
    if (type === 100) {
      const d100 = this.createD100Dice(1);
      diceMesh = d100.diceMesh;
      body = d100.body1;
      body2 = d100.body2;
    } else {
      const dice = this.createDice(type);
      diceMesh = dice.diceMesh;
      body = dice.body;
    }
  
    // 벽 안에서 생성
    const startX = (Math.random() * (this.wallSize.width - 4) - (this.wallSize.width / 2 - 2));
    const startY = 1 + Math.random() * 1;
    const startZ = (Math.random() * (this.wallSize.depth - 4) - (this.wallSize.depth / 2 - 2));
  

    if (type === 100) {
      // D100의 경우 두 주사위를 약간 떨어뜨려 배치
      diceMesh.children[0].position.set(startX - 0.6, startY, startZ);
      diceMesh.children[1].position.set(startX + 0.6, startY, startZ);
      body.position.set(startX - 0.6, startY, startZ);
      body2.position.set(startX + 0.6, startY, startZ);
    } else {
      diceMesh.position.set(startX, startY, startZ);
      body.position.set(startX, startY, startZ);
    }

    this.scene.add(diceMesh);
    this.diceMeshes.push(diceMesh);
    this.diceBodies.push(body);
    this.world.addBody(body);
  
    if (body2) {
      this.diceBodies.push(body2);
      this.world.addBody(body2);
    }
  
    // 주사위에 힘을 가함
    const maxForce = 100;
    const force = new CANNON.Vec3(
      (Math.random() - 0.5) * maxForce,
      (Math.random() - 0.5) * maxForce / 90,
      (Math.random() - 0.5) * maxForce
    );
    body.applyImpulse(force, new CANNON.Vec3(0, 0, 0));
    if (body2) {
      body2.applyImpulse(force, new CANNON.Vec3(0, 0, 0));
    }
  
    // 주사위에 회전 속도를 설정
    const angularVelocity = new CANNON.Vec3(
      Math.random() * 5 - 2.5,
      Math.random() * 5 - 2.5,
      Math.random() * 5 - 2.5
    );
    body.angularVelocity.set(angularVelocity.x, angularVelocity.y, angularVelocity.z);
    if (body2) {
    const angularVelocity2 = new CANNON.Vec3(
      Math.random() * 5 - 2.5,
      Math.random() * 5 - 2.5,
      Math.random() * 5 - 2.5
    );
      body2.angularVelocity.set(angularVelocity.x, angularVelocity.y, angularVelocity.z);
    }
  
    return new Promise(resolve => {
      const checkStability = setInterval(() => {
        if (body.velocity.lengthSquared() < 0.001 && body.angularVelocity.lengthSquared() < 0.001 &&
           (!body2 || (body2.velocity.lengthSquared() < 0.001 && body2.angularVelocity.lengthSquared() < 0.001))) {
          clearInterval(checkStability);
          const result = this.getTopFaceValue(diceMesh);
          resolve({ type, value: result });
        }
      }, 100);
      
      // 안전장치: 10초 후에도 안정화되지 않으면 강제로 결과 반환
      setTimeout(() => {
        clearInterval(checkStability);
        const result = this.getTopFaceValue(diceMesh);
        resolve({ type, value: result });
      }, 10000);
    });
  }

  getTopFaceValue(diceMesh) {
    const faceNormals = {
      BoxGeometry: [
        new THREE.Vector3(0, 1, 0),  // top
        new THREE.Vector3(0, -1, 0), // bottom
        new THREE.Vector3(1, 0, 0),  // right
        new THREE.Vector3(-1, 0, 0), // left
        new THREE.Vector3(0, 0, 1),  // front
        new THREE.Vector3(0, 0, -1)  // back
      ],
      // 다른 면체의 법선도 정의
    };

    const normals = faceNormals[diceMesh.geometry.type] || [];
  let maxDot = -Infinity;
  let topFaceIndex = -1;

  normals.forEach((normal, index) => {
    const transformedNormal = normal.clone().applyQuaternion(diceMesh.quaternion);
    const dot = transformedNormal.dot(new THREE.Vector3(0, 1, 0));
    if (dot > maxDot) {
      maxDot = dot;
      topFaceIndex = index;
    }
  });

  return diceMesh.userData.faceValues[topFaceIndex];
  }

  clearDice() {
    this.diceMeshes.forEach(dice => {
      this.scene.remove(dice); // 주사위 메쉬를 장면에서 제거
      if (dice.geometry) dice.geometry.dispose(); // 주사위 지오메트리 삭제
      if (Array.isArray(dice.material)) {
        dice.material.forEach(mat => mat.dispose()); // 주사위 재질 배열 삭제
      } else if (dice.material) {
        dice.material.dispose(); // 주사위 재질 삭제
      }
    });
    this.diceMeshes = []; // 주사위 메쉬 배열 초기화
    this.diceBodies.forEach(body => {
      this.world.removeBody(body); // 주사위 바디를 물리 세계에서 제거
    });
    this.diceBodies = []; // 주사위 바디 배열 초기화
  }

  onWindowResize() {
    this.updateCameraAndWalls(); // 카메라와 벽을 업데이트
    this.renderer.setSize(this.container.clientWidth, this.container.clientHeight); // 렌더러 크기 조정
  }

  animate() {
    requestAnimationFrame(this.animate);
    TWEEN.update();
  
    const maxSubSteps = 3;
    const fixedTimeStep = 1 / 60;
    const deltaTime = Math.min(0.1, this.clock.getDelta());
  
    try {
      this.world.step(fixedTimeStep, deltaTime, maxSubSteps);
  
      this.diceBodies.forEach((body, index) => {
        const diceMesh = this.diceMeshes[index];
        if (diceMesh && body) {
          if (diceMesh instanceof THREE.Group) {
            // D100 주사위의 경우
            diceMesh.children[0].position.copy(body.position);
            diceMesh.children[0].quaternion.copy(body.quaternion);
            if (index + 1 < this.diceBodies.length) {
              const nextBody = this.diceBodies[index + 1];
              diceMesh.children[1].position.copy(nextBody.position);
              diceMesh.children[1].quaternion.copy(nextBody.quaternion);
            }
          } else {
            diceMesh.position.copy(body.position);
            diceMesh.quaternion.copy(body.quaternion);
          }
        }
      });
  
      this.renderer.render(this.scene, this.camera);
    } catch (error) {
      console.error("Error in animation loop:", error);
      cancelAnimationFrame(this.animationFrameId);
    }
  }
}

export default ThreeJSManager;
