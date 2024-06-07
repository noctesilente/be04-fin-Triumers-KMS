import './assets/main.css';

// npm install vue bootstrap bootstrap-vue-3
// npm install axios
// npm install vue-router@next
// npm install html2pdf.js
// npm install vuex

import { createApp } from 'vue';
import { createStore } from 'vuex'
import router from './router/mainRouter.js';
import App from './App.vue';
import axios from 'axios';
import store from './store/index.js';

axios.defaults.baseURL = 'http://triumers-back.ap-northeast-2.elasticbeanstalk.com/api/endpoint'; // 백엔드 서버의 주소와 포트를 지정합니다.
axios.defaults.withCredentials = true; // 쿠키를 포함하여 요청을 보내도록 설정합니다.

import BootstrapVue3 from 'bootstrap-vue-3';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css';

const app = createApp(App);

app.use(router);
app.use(createStore(store));
app.use(store);
app.use(BootstrapVue3);
app.mount('#app');