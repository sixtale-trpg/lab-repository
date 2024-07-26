import { createApp } from 'vue';
import store from './store';
import App from './App.vue';
import axios from 'axios';
import VueAxios from 'vue-axios';
import router from './common/lib/vue-router';
import ElementPlus from './common/lib/element-plus'; 
import BootstrapVue3 from 'bootstrap-vue-3';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.bundle.js';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';


const app = createApp(App);

app.use(ElementPlus.ElementPlus); // Element Plus 플러그인 등록
app.use(VueAxios, axios);
app.use(store);
app.use(router);
app.use(BootstrapVue3);

app.mount('#app');
