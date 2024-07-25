import * as THREE from 'three';
import TWEEN from '@tweenjs/tween.js';
import * as CANNON from 'cannon';

class ThreeJSManager {
  constructor(container) {
    this.container = container;
    this.scene = new THREE.Scene();
    this.camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
    this.camera.position.z = 5;
    this.renderer = new THREE.WebGLRenderer({ alpha: true }); // Add alpha: true to handle transparency
    this.renderer.setSize(container.clientWidth, container.clientHeight);
    container.appendChild(this.renderer.domElement);
    const light = new THREE.AmbientLight(0x404040);
    this.scene.add(light);
    this.diceMeshes = [];
    this.diceBodies = [];
    this.world = new CANNON.World();
    this.world.gravity.set(0, -9.82, 0);

    this.addFloor();
    this.addWalls(); // Add this to add walls to restrict movement
    this.animate = this.animate.bind(this);
    this.animate();
  }

  addFloor() {
    const floorShape = new CANNON.Plane();
    const floorBody = new CANNON.Body({ mass: 0 });
    floorBody.addShape(floorShape);
    floorBody.quaternion.setFromEuler(-Math.PI / 2, 0, 0);
    floorBody.position.y = -5;
    this.world.addBody(floorBody);

    const floorGeometry = new THREE.PlaneGeometry(20, 20);
    const floorMaterial = new THREE.MeshBasicMaterial({ color: 0x808080, side: THREE.DoubleSide, opacity: 0, transparent: true }); // Make floor transparent
    const floorMesh = new THREE.Mesh(floorGeometry, floorMaterial);
    floorMesh.rotation.x = -Math.PI / 2;
    floorMesh.position.y = -5;
    this.scene.add(floorMesh);
  }

  addWalls() {
    const wallHeight = 10;
    const halfSize = 10;
    const depth = 20;

    // Define walls as CANNON bodies
    const walls = [
      { x: -halfSize, y: wallHeight / 2, z: 0, rotation: { x: 0, y: Math.PI / 2, z: 0 } }, // Left wall
      { x: halfSize, y: wallHeight / 2, z: 0, rotation: { x: 0, y: -Math.PI / 2, z: 0 } }, // Right wall
      { x: 0, y: wallHeight / 2, z: -depth / 2, rotation: { x: 0, y: 0, z: 0 } }, // Front wall
      { x: 0, y: wallHeight / 2, z: depth / 2, rotation: { x: 0, y: Math.PI, z: 0 } }, // Back wall
      { x: 0, y: wallHeight, z: 0, rotation: { x: Math.PI / 2, y: 0, z: 0 } }, // Ceiling
      { x: 0, y: 0, z: 0, rotation: { x: -Math.PI / 2, y: 0, z: 0 } } // Floor
    ];
  
    walls.forEach(({ x, y, z, rotation }) => {
      const wallShape = new CANNON.Plane();
      const wallBody = new CANNON.Body({ mass: 0 });
      wallBody.addShape(wallShape);
      wallBody.position.set(x, y, z);
      wallBody.quaternion.setFromEuler(rotation.x, rotation.y, rotation.z);
      this.world.addBody(wallBody);
    });
  }

  createD10Geometry() {
    const radius = 1;
    const height = 1.5;
    const topCone = new THREE.ConeGeometry(radius, height, 5, 1, true);
    topCone.translate(0, height / 2, 0);
    const bottomCone = new THREE.ConeGeometry(radius, height, 5, 1, true);
    bottomCone.rotateX(Math.PI);
    bottomCone.translate(0, -height / 2, 0);
    topCone.rotateY(Math.PI / 5);
    const geometry = this.mergeBufferGeometries([topCone, bottomCone]);
    return geometry;
  }

  mergeBufferGeometries(geometries) {
    const mergedGeometry = new THREE.BufferGeometry();
    const mergedVertices = [];
    const mergedIndices = [];
    let indexOffset = 0;

    geometries.forEach(geometry => {
      const vertices = geometry.attributes.position.array;
      const indices = geometry.index ? geometry.index.array : [];

      for (let i = 0; i < vertices.length; i += 3) {
        mergedVertices.push(vertices[i], vertices[i + 1], vertices[i + 2]);
      }

      for (let i = 0; i < indices.length; i++) {
        mergedIndices.push(indices[i] + indexOffset);
      }

      indexOffset += vertices.length / 3;
    });

    mergedGeometry.setAttribute('position', new THREE.Float32BufferAttribute(mergedVertices, 3));
    mergedGeometry.setIndex(mergedIndices);
    mergedGeometry.computeVertexNormals();

    return mergedGeometry;
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

    // Ensure that all faces have the correct number of vertices
    cannonFaces.forEach(face => {
      if (face.length !== 3) {
        throw new Error('Invalid face definition: ' + JSON.stringify(face));
      }
    });

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

  createDice(type) {
    let geometry;
    let shape;
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
    const material = new THREE.MeshBasicMaterial({ color: Math.random() * 0xffffff, side: THREE.DoubleSide });
    const diceMesh = new THREE.Mesh(geometry, material);

    const edges = new THREE.EdgesGeometry(geometry);
    const lineMaterial = new THREE.LineBasicMaterial({ color: 0x000000 });
    const lineSegments = new THREE.LineSegments(edges, lineMaterial);
    diceMesh.add(lineSegments);

    const mass = 1;
    const body = new CANNON.Body({ mass });
    body.addShape(shape);

    // Ensure the body's quaternion is set to identity
    body.quaternion.set(0, 0, 0, 1);

    return { diceMesh, body };
  }

  rollDice(types) {
    this.clearDice();

    const promises = types.map((type, index) => this.rollSingleDice(type, index, types.length));

    return Promise.all(promises);
  }

  rollSingleDice(type, index, total) {
    const { diceMesh, body } = this.createDice(type);
    const startX = (Math.random() - 0.5) * 10; // 주사위가 화면 중앙에 위치하도록 조정
    const startY = 5; // Y 축 위에서 시작
    const startZ = (Math.random() - 0.5) * 10 + 10; // 화면 정면 바깥에서 시작

    diceMesh.position.set(startX, startY, startZ);
    body.position.set(startX, startY, startZ);
    this.scene.add(diceMesh);
    this.diceMeshes.push(diceMesh);
    this.diceBodies.push(body);
    this.world.addBody(body);

    const force = new CANNON.Vec3((Math.random() - 0.5) * 2, (Math.random() - 0.5) * 2, -(Math.random() * 2 + 1)); // Z 축 방향으로 던지는 힘
    body.applyImpulse(force, new CANNON.Vec3(0, 0, 0));

    return new Promise(resolve => {
      setTimeout(() => {
        this.scene.remove(diceMesh);
        this.world.removeBody(body);
        this.diceMeshes = this.diceMeshes.filter(d => d !== diceMesh);
        this.diceBodies = this.diceBodies.filter(b => b !== body);
        const result = Math.floor(Math.random() * type) + 1;
        resolve({ type, value: result });
      }, 7000); // 700ms -> 7000ms로 수정하여 더 긴 시간을 줍니다.
    });
  }

  clearDice() {
    this.diceMeshes.forEach(dice => {
      this.scene.remove(dice);
    });
    this.diceMeshes = [];
    this.diceBodies.forEach(body => {
      this.world.removeBody(body);
    });
    this.diceBodies = [];
  }

  animate() {
    requestAnimationFrame(this.animate);
    TWEEN.update();
    this.world.step(1 / 60);
    this.diceBodies.forEach((body, index) => {
      const diceMesh = this.diceMeshes[index];
      if (diceMesh && body) { // Ensure diceMesh and body are not undefined
        diceMesh.position.copy(body.position);
        diceMesh.quaternion.copy(body.quaternion);
      }
    });
    this.renderer.render(this.scene, this.camera);
  }
}

export default ThreeJSManager;
