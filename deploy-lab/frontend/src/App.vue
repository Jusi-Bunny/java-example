<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'
import { api, toApiError } from './api'
import type { SystemInfo } from './types'

const systemInfo = ref<SystemInfo | null>(null)
const online = ref(false)
const error = ref('')
let timer: number | undefined

const navItems = [
  { to: '/dashboard', label: '运行状态' },
  { to: '/request-inspector', label: '请求检查' },
  { to: '/messages', label: '临时消息' },
  { to: '/request-records', label: '请求记录' },
  { to: '/diagnostics', label: '诊断工具' }
]

const instanceLabel = computed(() => systemInfo.value?.instanceId ?? 'unknown')

async function refreshStatus() {
  try {
    const response = await api.systemInfo()
    systemInfo.value = response.data
    online.value = true
    error.value = ''
  } catch (err) {
    online.value = false
    error.value = toApiError(err).message
  }
}

onMounted(() => {
  refreshStatus()
  timer = window.setInterval(refreshStatus, 10000)
})

onUnmounted(() => {
  if (timer) {
    window.clearInterval(timer)
  }
})
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <span class="brand-mark">DL</span>
        <div>
          <strong>Deploy Lab</strong>
          <span>部署实验室</span>
        </div>
      </div>
      <nav class="nav-list">
        <RouterLink v-for="item in navItems" :key="item.to" :to="item.to">
          {{ item.label }}
        </RouterLink>
      </nav>
    </aside>

    <div class="workspace">
      <header class="topbar">
        <div>
          <span class="eyebrow">backend link</span>
          <h1>部署实验控制台</h1>
        </div>
        <div class="status-strip" :class="{ offline: !online }">
          <span class="status-dot" />
          <span>{{ online ? '后端在线' : '后端离线' }}</span>
          <strong>{{ instanceLabel }}</strong>
        </div>
      </header>

      <p v-if="error" class="notice error">{{ error }}</p>
      <main>
        <RouterView :system-info="systemInfo" @refresh-status="refreshStatus" />
      </main>
    </div>
  </div>
</template>
