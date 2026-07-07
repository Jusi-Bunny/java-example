<script setup lang="ts">
import { ref } from 'vue'
import { api, toApiError } from '../api'
import type { ApiResponse, HttpInspectResult } from '../types'

const method = ref('GET')
const query = ref('source=deploy-lab')
const contentType = ref('application/json')
const customHeaders = ref('X-Demo-Header: deploy-lab')
const body = ref('{\n  "message": "hello deploy lab"\n}')
const loading = ref(false)
const elapsed = ref<number | null>(null)
const result = ref<ApiResponse<HttpInspectResult> | null>(null)
const error = ref('')

function parseHeaders() {
  const headers: Record<string, string> = {}
  customHeaders.value.split('\n').forEach((line) => {
    const index = line.indexOf(':')
    if (index > 0) {
      headers[line.slice(0, index).trim()] = line.slice(index + 1).trim()
    }
  })
  if (contentType.value) {
    headers['Content-Type'] = contentType.value
  }
  return headers
}

async function send() {
  loading.value = true
  error.value = ''
  result.value = null
  const started = performance.now()
  try {
    const url = `/api/http/inspect${query.value ? `?${query.value}` : ''}`
    result.value = await api.inspect(method.value, url, ['GET'].includes(method.value) ? '' : body.value, {
      headers: parseHeaders()
    })
    elapsed.value = Math.round(performance.now() - started)
  } catch (err) {
    const apiError = toApiError(err)
    error.value = `${apiError.status ?? ''} ${apiError.message}`.trim()
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="page">
    <div class="section-head">
      <div>
        <h2>请求检查</h2>
        <p class="meta">发送不同方法、请求头和请求体，查看后端实际收到的内容。</p>
      </div>
    </div>

    <div class="split">
      <form class="tool-panel" @submit.prevent="send">
        <div class="form-grid">
          <label class="field">
            <span>Method</span>
            <select v-model="method">
              <option>GET</option>
              <option>POST</option>
              <option>PUT</option>
              <option>DELETE</option>
            </select>
          </label>
          <label class="field">
            <span>Query</span>
            <input v-model="query" placeholder="a=1&b=2" />
          </label>
          <label class="field full">
            <span>Content-Type</span>
            <input v-model="contentType" />
          </label>
          <label class="field full">
            <span>自定义请求头</span>
            <textarea v-model="customHeaders" />
          </label>
          <label class="field full">
            <span>请求体</span>
            <textarea v-model="body" :disabled="method === 'GET'" />
          </label>
        </div>
        <div class="actions">
          <button :disabled="loading">{{ loading ? '发送中' : '发送请求' }}</button>
          <span v-if="elapsed" class="meta">耗时 {{ elapsed }}ms</span>
        </div>
      </form>

      <article class="tool-panel">
        <h3>服务端接收结果</h3>
        <p v-if="error" class="notice error">{{ error }}</p>
        <pre v-if="result">{{ JSON.stringify(result, null, 2) }}</pre>
        <p v-else-if="!error" class="empty">发送请求后显示响应状态、请求头、客户端 IP、实例编号和 traceId。</p>
      </article>
    </div>
  </section>
</template>
