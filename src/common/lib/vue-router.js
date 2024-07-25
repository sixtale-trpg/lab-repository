import { createRouter, createWebHistory } from 'vue-router';
import Home from '@/views/home/home';
import Rulebook from '@/views/menu/Rulebook';
import Scenarios from '@/views/menu/Scenarios';
import FindPlayers from '@/views/menu/FindPlayers';
import Lobby from '@/views/menu/Lobby';
import Main from '@/views/main/main'; // main.vue를 공통 레이아웃으로 사용
import CharacterSheet from '@/views/games/CharacterSheet';
import InGame from '@/views/games/InGame';
import Waiting from '@/views/games/Waiting';

const routes = [
  {
    path: '/',
    component: Main,
    children: [
      {
        path: '',
        name: 'Home',
        component: Home
      },
      {
        path: 'rulebook',
        name: 'Rulebook',
        component: Rulebook
      },
      {
        path: 'scenarios',
        name: 'Scenarios',
        component: Scenarios
      },
      {
        path: 'find-players',
        name: 'FindPlayers',
        component: FindPlayers
      },
      {
        path: 'lobby',
        name: 'Lobby',
        component: Lobby
      }
    ]
  },
  {
    path: '/game',
    children: [
      {
        path: ':roomId/waiting',
        name: 'Waiting',
        component: Waiting
      },
      {
        path: ':roomId/character-sheet',
        name: 'CharacterSheet',
        component: CharacterSheet
      },
      {
        path: ':roomId/in-game',
        name: 'InGame',
        component: InGame
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.afterEach((to) => {
  console.log(to);
});

export default router;
