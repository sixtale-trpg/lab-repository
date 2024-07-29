import * as THREE from 'three';  // 3D 그래픽을 다루는 라이브러리 THREE.js를 임포트합니다.
import TWEEN from '@tweenjs/tween.js';  // 애니메이션을 다루는 라이브러리 tween.js를 임포트합니다.
import * as CANNON from 'cannon';  // 물리 엔진을 다루는 라이브러리 cannon.js를 임포트합니다.
import eventBus from '@/common/lib/eventBus.js';  // 이벤트 버스를 임포트합니다.

class ThreeJSManager {
  constructor(container) {
    this.container = container;  // 3D 렌더링을 할 HTML 컨테이너를 저장합니다.

    this.scene = new THREE.Scene();  // THREE.js의 Scene 객체를 생성합니다.
    this.camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);  // PerspectiveCamera 객체를 생성합니다.

    this.camera.position.set(0, 10, -2);  // 카메라의 위치를 설정합니다.
    this.camera.lookAt(0, 0, 0);  // 카메라의 시점을 설정합니다.
    this.camera.rotation.x = -Math.PI / 2;  // 카메라의 회전을 설정합니다.

    this.renderer = new THREE.WebGLRenderer({ alpha: true });  // WebGLRenderer 객체를 생성합니다.
    this.renderer.setSize(container.clientWidth, container.clientHeight);  // 렌더러의 크기를 설정합니다.
    container.appendChild(this.renderer.domElement);  // 컨테이너에 렌더러를 추가합니다.

    const light = new THREE.AmbientLight(0x404040);  // AmbientLight를 생성합니다.
    this.scene.add(light);  // 씬에 조명을 추가합니다.

    this.diceMeshes = [];  // 주사위 메쉬를 저장할 배열을 초기화합니다.
    this.diceBodies = [];  // 주사위 물리 바디를 저장할 배열을 초기화합니다.

    this.world = new CANNON.World();  // CANNON.js의 World 객체를 생성합니다.
    this.world.gravity.set(0, -20, 11);  // 중력을 설정합니다.

    this.diceMaterial = new CANNON.Material('diceMaterial');  // 주사위 재질을 생성합니다.
    this.floorMaterial = new CANNON.Material('floorMaterial');  // 바닥 재질을 생성합니다.
    this.contactMaterial = new CANNON.ContactMaterial(this.diceMaterial, this.floorMaterial, {  // 접촉 재질을 생성합니다.
      friction: 0.4,  // 마찰 계수를 설정합니다.
      restitution: 0.1,  // 반발 계수를 설정합니다.
    });

    this.addFloor();  // 바닥을 추가합니다.
    this.addWalls();  // 벽을 추가합니다.

    this.animate = this.animate.bind(this);  // 애니메이션 루프를 바인딩합니다.
    this.animate();  // 애니메이션 루프를 시작합니다.

    eventBus.on('change-camera', this.changeCameraPosition.bind(this));  // change-camera 이벤트를 구독합니다.
    eventBus.on('roll-dice', this.rollDice.bind(this));  // roll-dice 이벤트를 구독합니다.
    this.camera.updateProjectionMatrix();
  }

  addFloor() {
    const floorShape = new CANNON.Plane();  // 바닥 모양을 생성합니다.
    const floorBody = new CANNON.Body({ mass: 0 });  // 바닥 바디를 생성합니다.
    floorBody.addShape(floorShape);  // 바디에 모양을 추가합니다.
    floorBody.quaternion.setFromEuler(-Math.PI / 2, 0, 0);  // 회전을 설정합니다.
    floorBody.position.y = -5;  // 위치를 설정합니다.
    this.world.addBody(floorBody);  // 월드에 바디를 추가합니다.
  
    const floorGeometry = new THREE.PlaneGeometry(10, 10);  // 바닥 기하학을 10x10 크기로 설정합니다.
    const floorMaterial = new THREE.MeshBasicMaterial({ color: 0x808080, side: THREE.DoubleSide, opacity: 1, transparent: false });  // 바닥 재질을 설정합니다.
    const floorMesh = new THREE.Mesh(floorGeometry, floorMaterial);  // 바닥 메쉬를 생성합니다.
    floorMesh.rotation.x = -Math.PI / 2;  // 회전을 설정합니다.
    floorMesh.position.y = -5;  // 위치를 설정합니다.
    this.scene.add(floorMesh);  // 씬에 바닥 메쉬를 추가합니다.
  }


  addWalls() {
    const wallHeight = 10;  // 벽 높이를 설정합니다.
    const halfSize = 5;  // 절반 크기를 설정합니다.
    const depth = 10;  // 깊이를 설정합니다.
  
    const walls = [
        { x: -halfSize, y: wallHeight / 2, z: 0, rotation: { x: 0, y: Math.PI / 2, z: 0 } },  // 왼쪽 벽
        { x: halfSize, y: wallHeight / 2, z: 0, rotation: { x: 0, y: -Math.PI / 2, z: 0 } },  // 오른쪽 벽
        { x: 0, y: wallHeight / 2, z: -halfSize, rotation: { x: 0, y: 0, z: 0 } },  // 앞쪽 벽
        { x: 0, y: wallHeight / 2, z: halfSize, rotation: { x: 0, y: Math.PI, z: 0 } },  // 뒤쪽 벽
        { x: 0, y: wallHeight, z: 0, rotation: { x: Math.PI / 2, y: 0, z: 0 } },  // 천장
        { x: 0, y: 0, z: 0, rotation: { x: -Math.PI / 2, y: 0, z: 0 } },  // 바닥
    ];
  
    walls.forEach(({ x, y, z, rotation }) => {
        const wallShape = new CANNON.Plane();  // 벽 모양을 생성합니다.
        const wallBody = new CANNON.Body({ mass: 0 });  // 벽 바디를 생성합니다.
        wallBody.addShape(wallShape);  // 바디에 모양을 추가합니다.
        wallBody.position.set(x, y, z);  // 위치를 설정합니다.
        wallBody.quaternion.setFromEuler(rotation.x, rotation.y, rotation.z);  // 회전을 설정합니다.
        this.world.addBody(wallBody);  // 월드에 바디를 추가합니다.
    });
  
    const wallGeometry = new THREE.PlaneGeometry(depth, wallHeight);  // 벽 기하학을 생성합니다.
    const wallMaterial = new THREE.MeshBasicMaterial({ color: 0xFF0000, side: THREE.DoubleSide, opacity: 1, transparent: false });  // 벽 재질을 빨간색으로 설정합니다.
  
    const leftWall = new THREE.Mesh(wallGeometry, wallMaterial);  // 왼쪽 벽 메쉬를 생성합니다.
    leftWall.rotation.y = Math.PI / 2;  // 회전을 설정합니다.
    leftWall.position.set(-halfSize, wallHeight / 2, 0);  // 위치를 설정합니다.
    this.scene.add(leftWall);  // 씬에 왼쪽 벽 메쉬를 추가합니다.
  
    const rightWall = new THREE.Mesh(wallGeometry, wallMaterial);  // 오른쪽 벽 메쉬를 생성합니다.
    rightWall.rotation.y = -Math.PI / 2;  // 회전을 설정합니다.
    rightWall.position.set(halfSize, wallHeight / 2, 0);  // 위치를 설정합니다.
    this.scene.add(rightWall);  // 씬에 오른쪽 벽 메쉬를 추가합니다.
  
    const frontWall = new THREE.Mesh(wallGeometry, wallMaterial);  // 앞쪽 벽 메쉬를 생성합니다.
    frontWall.rotation.y = 0;  // 회전을 설정합니다.
    frontWall.position.set(0, wallHeight / 2, -halfSize);  // 위치를 설정합니다.
    this.scene.add(frontWall);  // 씬에 앞쪽 벽 메쉬를 추가합니다.
  
    const backWall = new THREE.Mesh(wallGeometry, wallMaterial);  // 뒤쪽 벽 메쉬를 생성합니다.
    backWall.rotation.y = Math.PI;  // 회전을 설정합니다.
    backWall.position.set(0, wallHeight / 2, halfSize);  // 위치를 설정합니다.
    this.scene.add(backWall);  // 씬에 뒤쪽 벽 메쉬를 추가합니다.
  
    const ceilingGeometry = new THREE.PlaneGeometry(depth, halfSize * 2);  // 천장 기하학을 생성합니다.
    const ceiling = new THREE.Mesh(ceilingGeometry, wallMaterial);  // 천장 메쉬를 생성합니다.
    ceiling.rotation.x = Math.PI / 2;  // 회전을 설정합니다.
    ceiling.position.set(0, wallHeight, 0);  // 위치를 설정합니다.
    this.scene.add(ceiling);  // 씬에 천장 메쉬를 추가합니다.
  }


  createD10Geometry() {
    const radius = 1;  // 반지름을 설정합니다.
    const height = 1.5;  // 높이를 설정합니다.
    const topCone = new THREE.ConeGeometry(radius, height, 5, 1, true);  // 상단 원뿔 기하학을 생성합니다.
    topCone.translate(0, height / 2, 0);  // 위치를 설정합니다.
    const bottomCone = new THREE.ConeGeometry(radius, height, 5, 1, true);  // 하단 원뿔 기하학을 생성합니다.
    bottomCone.rotateX(Math.PI);  // 회전을 설정합니다.
    bottomCone.translate(0, -height / 2, 0);  // 위치를 설정합니다.
    topCone.rotateY(Math.PI / 5);  // 회전을 설정합니다.

    const mergedGeometry = new THREE.BufferGeometry();  // 병합된 기하학을 생성합니다.
    const vertices = new Float32Array([
      ...topCone.attributes.position.array,
      ...bottomCone.attributes.position.array
    ]);  // 병합된 버텍스를 생성합니다.

    const topIndices = topCone.index.array;  // 상단 원뿔 인덱스를 생성합니다.
    const bottomIndices = bottomCone.index.array.map(index => index + topCone.attributes.position.count / 3);  // 하단 원뿔 인덱스를 생성합니다.

    const indices = new Uint16Array([
      ...topIndices,
      ...bottomIndices
    ]);  // 병합된 인덱스를 생성합니다.

    mergedGeometry.setAttribute('position', new THREE.BufferAttribute(vertices, 3));  // 병합된 기하학에 버텍스를 설정합니다.
    mergedGeometry.setIndex(new THREE.BufferAttribute(indices, 1));  // 병합된 기하학에 인덱스를 설정합니다.
    mergedGeometry.computeVertexNormals();  // 버텍스 노멀을 계산합니다.

    return mergedGeometry;  // 병합된 기하학을 반환합니다.
  }

  createConvexPolyhedron(geometry) {
    if (!geometry.index) {
      console.warn('Geometry index is missing. Generating index.');  // 기하학 인덱스가 없는 경우 경고를 출력합니다.
      geometry = this.createIndexedBufferGeometry(geometry);  // 인덱스 버퍼 기하학을 생성합니다.
    }
    if (!geometry.attributes.position) {
      throw new Error('Geometry position attribute is missing.');  // 기하학 위치 속성이 없는 경우 오류를 발생시킵니다.
    }

    const vertices = Array.from(geometry.attributes.position.array);  // 버텍스 배열을 생성합니다.
    const indices = Array.from(geometry.index.array);  // 인덱스 배열을 생성합니다.
    const cannonVertices = [];  // CANNON.js 버텍스 배열을 생성합니다.
    for (let i = 0; i < vertices.length; i += 3) {
      cannonVertices.push(new CANNON.Vec3(vertices[i], vertices[i + 1], vertices[i + 2]));  // CANNON.js 버텍스를 추가합니다.
    }
    const cannonFaces = [];  // CANNON.js 얼굴 배열을 생성합니다.
    for (let i = 0; i < indices.length; i += 3) {
      cannonFaces.push([indices[i], indices[i + 1], indices[i + 2]]);  // CANNON.js 얼굴을 추가합니다.
    }

    cannonFaces.forEach(face => {
      if (face.length !== 3) {
        throw new Error('Invalid face definition: ' + JSON.stringify(face));  // 유효하지 않은 얼굴 정의가 있는 경우 오류를 발생시킵니다.
      }
    });

    cannonVertices.forEach(vertex => {
      if (vertex.x === undefined || vertex.y === undefined || vertex.z === undefined) {
        throw new Error('Undefined vertex value detected');  // 정의되지 않은 버텍스 값이 있는 경우 오류를 발생시킵니다.
      }
    });

    return new CANNON.ConvexPolyhedron(cannonVertices, cannonFaces);  // CANNON.js 볼록 다면체를 반환합니다.
  }

  createIndexedBufferGeometry(geometry) {
    if (geometry.index) {
      console.warn('THREE.BufferGeometry.toNonIndexed(): BufferGeometry is already non-indexed.');  // 버퍼 기하학이 이미 비인덱스화된 경우 경고를 출력합니다.
      return geometry;  // 기하학을 반환합니다.
    }

    const nonIndexed = geometry.toNonIndexed();  // 비인덱스 기하학을 생성합니다.
    const position = nonIndexed.attributes.position;  // 위치 속성을 가져옵니다.
    const indices = [];  // 인덱스 배열을 생성합니다.

    for (let i = 0; i < position.count; i++) {
      indices.push(i);  // 인덱스를 추가합니다.
    }

    nonIndexed.setIndex(indices);  // 인덱스를 설정합니다.
    return nonIndexed;  // 비인덱스 기하학을 반환합니다.
  }

  createDice(type) {
    let geometry;
    let shape;
    switch (type) {
      case 4:
        geometry = new THREE.TetrahedronGeometry(1);  // Tetrahedron 기하학을 생성합니다.
        shape = this.createConvexPolyhedron(geometry);  // 볼록 다면체를 생성합니다.
        break;
      case 6:
        geometry = new THREE.BoxGeometry(1, 1, 1);  // Box 기하학을 생성합니다.
        shape = new CANNON.Box(new CANNON.Vec3(0.5, 0.5, 0.5));  // CANNON.js 박스를 생성합니다.
        break;
      case 8:
        geometry = new THREE.OctahedronGeometry(1);  // Octahedron 기하학을 생성합니다.
        shape = this.createConvexPolyhedron(geometry);  // 볼록 다면체를 생성합니다.
        break;
      case 10:
        geometry = this.createD10Geometry();  // D10 기하학을 생성합니다.
        shape = this.createConvexPolyhedron(geometry);  // 볼록 다면체를 생성합니다.
        break;
      case 12:
        geometry = new THREE.DodecahedronGeometry(1);  // Dodecahedron 기하학을 생성합니다.
        shape = this.createConvexPolyhedron(geometry);  // 볼록 다면체를 생성합니다.
        break;
      case 20:
        geometry = new THREE.IcosahedronGeometry(1);  // Icosahedron 기하학을 생성합니다.
        shape = this.createConvexPolyhedron(geometry);  // 볼록 다면체를 생성합니다.
        break;
      default:
        geometry = new THREE.BoxGeometry(1, 1, 1);  // 기본적으로 Box 기하학을 생성합니다.
        shape = new CANNON.Box(new CANNON.Vec3(0.5, 0.5, 0.5));  // 기본적으로 CANNON.js 박스를 생성합니다.
    }
    const material = new THREE.MeshBasicMaterial({ color: 804000, side: THREE.DoubleSide });  // 주사위 재질을 생성합니다.
    const diceMesh = new THREE.Mesh(geometry, material);  // 주사위 메쉬를 생성합니다.

    const edges = new THREE.EdgesGeometry(geometry);  // 주사위 모서리 기하학을 생성합니다.
    const lineMaterial = new THREE.LineBasicMaterial({ color: 0x000000 });  // 주사위 모서리 재질을 생성합니다.
    const lineSegments = new THREE.LineSegments(edges, lineMaterial);  // 주사위 모서리 메쉬를 생성합니다.
    diceMesh.add(lineSegments);  // 주사위 메쉬에 모서리 메쉬를 추가합니다.

    const mass = 1;  // 질량을 설정합니다.
    const body = new CANNON.Body({ mass });  // 주사위 바디를 생성합니다.
    body.addShape(shape);  // 바디에 모양을 추가합니다.

    body.quaternion.set(0, 0, 0, 1);  // 바디의 회전을 설정합니다.

    return { diceMesh, body };  // 주사위 메쉬와 바디를 반환합니다.
  }

  rollDice(types) {
    this.clearDice();  // 기존 주사위를 초기화합니다.

    const promises = types.map((type, index) => {
      if (type === 100) {
        // D100 주사위를 위한 두 개의 D10 주사위를 굴립니다.
        return Promise.all([
          this.rollSingleDice(10, index, types.length),
          this.rollSingleDice(10, index, types.length)
        ]).then(results => {
          const tens = results[0].value - 1; // 첫 번째 D10의 결과 (0-9)
          const units = results[1].value - 1; // 두 번째 D10의 결과 (0-9)
          const d100Result = tens * 10 + units + 1; // 최종 D100 결과 (1-100)
          return { type: 100, value: d100Result };  // D100 결과를 반환합니다.
        });
      } else {
        return this.rollSingleDice(type, index, types.length);  // 개별 주사위를 굴립니다.
      }
    });

    return Promise.all(promises);  // 모든 주사위 결과를 반환합니다.
  }

  rollSingleDice(type, index, total) {
    const { diceMesh, body } = this.createDice(type);  // 주사위를 생성합니다.
    const startX = -4.5 + (Math.random() * 0.1);  // 남서 방향 시작 위치를 설정합니다.
    const startY = 1 + Math.random() * 0.01;  // 시작 위치를 설정합니다.
    const startZ = 5 + (Math.random() * 0.01);  // 남서 방향 시작 위치를 설정합니다.
  
    diceMesh.position.set(startX, startY, startZ);  // 주사위 메쉬의 위치를 설정합니다.
    body.position.set(startX, startY, startZ);  // 주사위 바디의 위치를 설정합니다.
    this.scene.add(diceMesh);  // 씬에 주사위 메쉬를 추가합니다.
    this.diceMeshes.push(diceMesh);  // 주사위 메쉬 배열에 추가합니다.
    this.diceBodies.push(body);  // 주사위 바디 배열에 추가합니다.
    this.world.addBody(body);  // 월드에 주사위 바디를 추가합니다.
  
    const force = new CANNON.Vec3(
      (Math.random() * 5) + 5,  // 힘을 설정합니다.
      (Math.random() * 0.01)+2,  // 힘을 설정합니다.
      -(Math.random() * 0.01 + 5)  // 힘을 설정합니다.
    );
    body.applyImpulse(force, new CANNON.Vec3(0, 0, 0));  // 주사위에 힘을 적용합니다.
  
    return new Promise(resolve => {
      setTimeout(() => {
        const result = Math.floor((Math.abs(body.quaternion.x) + Math.abs(body.quaternion.y) + Math.abs(body.quaternion.z)) % type) + 1;  // 결과를 계산합니다.
        this.scene.remove(diceMesh);  // 씬에서 주사위 메쉬를 제거합니다.
        this.world.removeBody(body);  // 월드에서 주사위 바디를 제거합니다.
        this.diceMeshes = this.diceMeshes.filter(d => d !== diceMesh);  // 주사위 메쉬 배열에서 제거합니다.
        this.diceBodies = this.diceBodies.filter(b => b !== body);  // 주사위 바디 배열에서 제거합니다.
        resolve({ type, value: result });  // 결과를 반환합니다.
      }, 70000);  // 7초 후에 결과를 반환합니다.
    });
  }

  clearDice() {
    this.diceMeshes.forEach(dice => {
      this.scene.remove(dice);  // 씬에서 주사위 메쉬를 제거합니다.
    });
    this.diceMeshes = [];  // 주사위 메쉬 배열을 초기화합니다.
    this.diceBodies.forEach(body => {
      this.world.removeBody(body);  // 월드에서 주사위 바디를 제거합니다.
    });
    this.diceBodies = [];  // 주사위 바디 배열을 초기화합니다.
  }

  changeCameraPosition() {
    // 카메라 이동을 비활성화합니다.
  }

  resetCameraPosition() {
    // 카메라 위치 초기화를 비활성화합니다.
  }

  animate() {
    requestAnimationFrame(this.animate);  // 애니메이션 프레임을 요청합니다.
    TWEEN.update();  // TWEEN 애니메이션을 업데이트합니다.
    this.world.step(1 / 60);  // 물리 시뮬레이션을 진행합니다.
    this.diceBodies.forEach((body, index) => {
      const diceMesh = this.diceMeshes[index];  // 주사위 메쉬를 가져옵니다.
      if (diceMesh && body) {
        diceMesh.position.copy(body.position);  // 주사위 메쉬의 위치를 업데이트합니다.
        diceMesh.quaternion.copy(body.quaternion);  // 주사위 메쉬의 회전을 업데이트합니다.
      }
    });
    this.renderer.render(this.scene, this.camera);  // 씬을 렌더링합니다.
  }
}

export default ThreeJSManager;  // ThreeJSManager 클래스를 내보냅니다.
