<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import { api, toApiError } from '../api'
import type { RequestRecord } from '../types'

const records = ref<RequestRecord[]>([])
const error = ref('')
const autoRefresh = ref(false)
const methodFilter = ref('')
const statusFilter = ref('')
const instanceFilter = ref('')
let timer: number | undefined

const filteredRecords = computed(() => records.value.filter((record) => {
  return (!methodFilter.value || record.method === methodFilter.value)
    && (!statusFilter.value || String(record.status) === statusFilter.value)
    && (!instanceFilter.value || record.instanceId.includes(instanceFilter.value))
}))

async function loadRecords() {
  try {
    records.value = (await api.listRequestRecords()).data
  } catch (err) {
    error.value = toApiError(err).message
  }
}

async function clearRecords() {
  await api.clearRequestRecords()
  await loadRecords()
}

function toggleAutoRefresh() {
  if (timer) {
    window.clearInterval(timer)
    timer = undefined
  }
  if (autoRefresh.value) {
    timer = window.setInterval(loadRecords, 3000)
  }
}

onMounted(loadRecords)
onUnmounted(() => {
  if (timer) {
    window.clearInterval(timer)
  }
})
</script>

<template>
  <section class="page">
    <div class="section-head">
      <div>
        <h2>请求记录</h2>
        <p class="meta">只保存最近请求元数据，不保存 Cookie、Authorization 和请求体。</p>
      </div>
      <div class="actions">
        <button class="secondary" @click="loadRecords">刷新</button>
        <button class="danger" @click="clearRecords">清空</button>
      </div>
    </div>

    <p v-if="error" class="notice error">{{ error }}</p>

    <div class="tool-panel">
      <div class="form-grid">
        <label class="field">
          <span>Method</span>
          <select v-model="methodFilter">
            <option value="">全部</option>
            <option>GET</option>
            <option>POST</option>
            <option>PUT</option>
            <option>DELETE</option>
          </select>
        </label>
        <label class="field">
          <span>Status</span>
          <input v-model="statusFilter" placeholder="200" />
        </label>
        <label class="field">
          <span>Instance</span>
          <input v-model="instanceFilter" placeholder="backend-01" />
        </label>
        <label class="field">
          <span>自动刷新</span>
          <select v-model="autoRefresh" @change="toggleAutoRefresh">
            <option :value="false">关闭</option>
            <option :value="true">每 3 秒</option>
          </select>
        </label>
      </div>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th>时间</th>
            <th>Method</th>
            <th>Path</th>
            <th>Status</th>
            <th>Duration</th>
            <th>Client IP</th>
            <th>Instance</th>
            <th>Trace ID</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in filteredRecords" :key="record.id">
            <td>{{ record.createdAt }}</td>
            <td>{{ record.method }}</td>
            <td class="code">{{ record.path }}</td>
            <td>{{ record.status }}</td>
            <td>{{ record.durationMs }}ms</td>
            <td>{{ record.clientIp }}</td>
            <td>{{ record.instanceId }}</td>
            <td class="code">{{ record.traceId }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>
