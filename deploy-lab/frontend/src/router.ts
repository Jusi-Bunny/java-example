import { createRouter, createWebHistory } from 'vue-router'
import DashboardView from './views/DashboardView.vue'
import DiagnosticsView from './views/DiagnosticsView.vue'
import MessagesView from './views/MessagesView.vue'
import RequestInspectorView from './views/RequestInspectorView.vue'
import RequestRecordsView from './views/RequestRecordsView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/dashboard' },
    { path: '/dashboard', component: DashboardView },
    { path: '/request-inspector', component: RequestInspectorView },
    { path: '/messages', component: MessagesView },
    { path: '/request-records', component: RequestRecordsView },
    { path: '/diagnostics', component: DiagnosticsView }
  ]
})

export default router
