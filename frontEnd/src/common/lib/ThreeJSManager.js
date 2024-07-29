import * as THREE from 'three';
import TWEEN from '@tweenjs/tween.js';
import * as CANNON from 'cannon';
import eventBus from '@/common/lib/eventBus.js';

class ThreeJSManager {
  constructor(container) {
    this.container = container;
    this.scene = new THREE.Scene();
    this.camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000);
    this.camera.position.z = 5;
    this.defaultCameraPosition = this.camera.position.clone();
    this.defaultCameraRotation = this.camera.rotation.clone();
    this.renderer = new THREE.WebGLRenderer({ alpha: true });
    this.renderer.setSize(container.clientWidth, container.clientHeight);
    container.appendChild(this.renderer.domElement);
    const light = new THREE.AmbientLight(0x404040);
    this.scene.add(light);
    this.diceMeshes = [];
    this.diceBodies = [];
    this.world = new CANNON.World();
    this.world.gravity.set(0, -15, 0);

    this.diceMaterial = new CANNON.Material('diceMaterial');
    this.floorMaterial = new CANNON.Material('floorMaterial');
    this.contactMaterial = new CANNON.ContactMaterial(this.diceMaterial, this.floorMaterial, {
      friction: 0.4,
      restitution: 0.1,
    });

    this.addFloor();
    this.addWalls();
    this.animate = this.animate.bind(this);
    this.animate();

    eventBus.on('change-camera', this.changeCameraPosition.bind(this));
    eventBus.on('roll-dice', this.rollDice.bind(this)); // roll-dice 이벤트 구독
  }

  addFloor() {
    const floorShape = new CANNON.Plane();
    const floorBody = new CANNON.Body({ mass: 0 });
    floorBody.addShape(floorShape);
    floorBody.quaternion.setFromEuler(-Math.PI / 2, 0, 0);
    floorBody.position.y = -5;
    this.world.addBody(floorBody);

    const floorGeometry = new THREE.PlaneGeometry(20, 20);
    const floorMaterial = new THREE.MeshBasicMaterial({ color: 0x808080, side: THREE.DoubleSide, opacity: 100, transparent: true });
    const floorMesh = new THREE.Mesh(floorGeometry, floorMaterial);
    floorMesh.rotation.x = -Math.PI / 2;
    floorMesh.position.y = -5;
    this.scene.add(floorMesh);
  }

  addWalls() {
    const wallHeight = 10;
    const halfSize = 8;
    const depth = 20;
    const wallWidth = 30;

    const walls = [
      { x: -halfSize, y: wallHeight / 2, z: 0, rotation: { x: 0, y: Math.PI / 2, z: 0 } },
      { x: halfSize, y: wallHeight / 2, z: 0, rotation: { x: 0, y: -Math.PI / 2, z: 0 } },
      { x: 0, y: wallHeight / 2, z: -depth / 2, rotation: { x: 0, y: 0, z: 0 } },
      { x: 0, y: wallHeight / 2, z: depth / 2, rotation: { x: 0, y: Math.PI, z: 0 } },
      { x: 0, y: wallHeight, z: 0, rotation: { x: Math.PI / 2, y: 0, z: 0 } },
      { x: 0, y: 0, z: 0, rotation: { x: -Math.PI / 2, y: 0, z: 0 } },
    ];

    walls.forEach(({ x, y, z, rotation }) => {
      const wallShape = new CANNON.Plane();
      const wallBody = new CANNON.Body({ mass: 0 });
      wallBody.addShape(wallShape);
      wallBody.position.set(x, y, z);
      wallBody.quaternion.setFromEuler(rotation.x, rotation.y, rotation.z);
      this.world.addBody(wallBody);
    });

    const wallGeometry = new THREE.PlaneGeometry(depth, wallHeight);
    const wallMaterial = new THREE.MeshBasicMaterial({ color: 0x808080, side: THREE.DoubleSide, opacity: 0, transparent: true });

    const leftWall = new THREE.Mesh(wallGeometry, wallMaterial);
    leftWall.rotation.y = Math.PI / 2;
    leftWall.position.set(-halfSize, wallHeight / 2, 0);
    this.scene.add(leftWall);

    const rightWall = new THREE.Mesh(wallGeometry, wallMaterial);
    rightWall.rotation.y = -Math.PI / 2;
    rightWall.position.set(halfSize, wallHeight / 2, 0);
    this.scene.add(rightWall);

    const frontWall = new THREE.Mesh(wallGeometry, wallMaterial);
    frontWall.rotation.y = 0;
    frontWall.position.set(0, wallHeight / 2, -depth / 2);
    this.scene.add(frontWall);

    const backWall = new THREE.Mesh(wallGeometry, wallMaterial);
    backWall.rotation.y = Math.PI;
    backWall.position.set(0, wallHeight / 2, depth / 2);
    this.scene.add(backWall);

    const ceilingGeometry = new THREE.PlaneGeometry(depth, halfSize * 2);
    const ceiling = new THREE.Mesh(ceilingGeometry, wallMaterial);
    ceiling.rotation.x = Math.PI / 2;
    ceiling.position.set(0, wallHeight, 0);
    this.scene.add(ceiling);
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

    const mergedGeometry = new THREE.BufferGeometry();
    const vertices = new Float32Array([
      ...topCone.attributes.position.array,
      ...bottomCone.attributes.position.array
    ]);

    const topIndices = topCone.index.array;
    const bottomIndices = bottomCone.index.array.map(index => index + topCone.attributes.position.count / 3);

    const indices = new Uint16Array([
      ...topIndices,
      ...bottomIndices
    ]);

    mergedGeometry.setAttribute('position', new THREE.BufferAttribute(vertices, 3));
    mergedGeometry.setIndex(new THREE.BufferAttribute(indices, 1));
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

    cannonFaces.forEach(face => {
      if (face.length !== 3) {
        throw new Error('Invalid face definition: ' + JSON.stringify(face));
      }
    });

    cannonVertices.forEach(vertex => {
      if (vertex.x === undefined || vertex.y === undefined || vertex.z === undefined) {
        throw new Error('Undefined vertex value detected');
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
    const material = new THREE.MeshBasicMaterial({ color: 804000, side: THREE.DoubleSide });
    const diceMesh = new THREE.Mesh(geometry, material);

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

    const promises = types.map((type, index) => {
      if (type === 100) {
        // D100 주사위를 위한 두 개의 D10 주사위를 굴린다
        return Promise.all([
          this.rollSingleDice(10, index, types.length),
          this.rollSingleDice(10, index, types.length)
        ]).then(results => {
          const tens = results[0].value - 1; // 첫 번째 D10의 결과 (0-9)
          const units = results[1].value - 1; // 두 번째 D10의 결과 (0-9)
          const d100Result = tens * 10 + units + 1; // 최종 D100 결과 (1-100)
          return { type: 100, value: d100Result };
        });
      } else {
        return this.rollSingleDice(type, index, types.length);
      }
    });

    return Promise.all(promises);
  }

  rollSingleDice(type, index, total) {
    const { diceMesh, body } = this.createDice(type);
    const startX = (Math.random() - 0.5) * 4;
    const startY = 5 + Math.random() * 0.01;
    const startZ = 10 + Math.random() * 2;

    diceMesh.position.set(startX, startY, startZ);
    body.position.set(startX, startY, startZ);
    this.scene.add(diceMesh);
    this.diceMeshes.push(diceMesh);
    this.diceBodies.push(body);
    this.world.addBody(body);

    const force = new CANNON.Vec3(
      (Math.random() - 0.5) * 1.5,
      (Math.random() - 0.5) * 2,
      -(Math.random() * 0.1 + 5)
    );
    body.applyImpulse(force, new CANNON.Vec3(0, 0, 0));

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
    });
    this.diceMeshes = [];
    this.diceBodies.forEach(body => {
      this.world.removeBody(body);
    });
    this.diceBodies = [];
  }

  changeCameraPosition() {
    new TWEEN.Tween(this.camera.position)
      .to({ x: 0, y: 20, z: 10 }, 3000)
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
      .to({ x: this.defaultCameraPosition.x, y: this.defaultCameraPosition.y, z: this.defaultCameraPosition.z }, 2000)
      .easing(TWEEN.Easing.Quadratic.Out)
      .start();

    new TWEEN.Tween(this.camera.rotation)
      .to({ x: this.defaultCameraRotation.x, y: this.defaultCameraRotation.y, z: this.defaultCameraRotation.z }, 2000)
      .easing(TWEEN.Easing.Quadratic.Out)
      .start();
  }

  animate() {
    requestAnimationFrame(this.animate);
    TWEEN.update();
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
