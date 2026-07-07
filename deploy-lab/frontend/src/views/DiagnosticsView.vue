<script setup lang="ts">
import { ref } from 'vue'
import { api, toApiError } from '../api'

const result = ref('')
const loading = ref(false)

async function run(label: string, action: () => Promise<unknown>) {
  loading.value = true
  const started = performance.now()
  try {
    const response = await action()
    result.value = JSON.stringify({
      label,
      elapsedMs: Math.round(performance.now() - started),
      response
    }, null, 2)
  } catch (err) {
    const apiError = toApiError(err)
    result.value = JSON.stringify({
      label,
      elapsedMs: Math.round(performance.now() - started),
      status: apiError.status,
      message: apiError.message,
      traceId: apiError.traceId
    }, null, 2)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <section class="page">
    <div class="section-head">
      <div>
        <h2>诊断工具</h2>
        <p class="meta">验证超时、状态码、异常处理、反向代理日志和 traceId 定位。</p>
      </div>
    </div>

    <div class="tool-panel">
      <h3>延迟接口</h3>
      <div class="actions">
        <button :disabled="loading" @click="run('延迟 500ms', () => api.delay(500))">延迟 500ms</button>
        <button :disabled="loading" @click="run('延迟 1 秒', () => api.delay(1000))">延迟 1 秒</button>
        <button :disabled="loading" @click="run('延迟 3 秒', () => api.delay(3000))">延迟 3 秒</button>
      </div>
    </div>

    <div class="tool-panel">
      <h3>状态码接口</h3>
      <div class="actions">
        <button class="secondary" @click="run('返回 400', () => api.status(400))">返回 400</button>
        <button class="secondary" @click="run('返回 404', () => api.status(404))">返回 404</button>
        <button class="secondary" @click="run('返回 500', () => api.status(500))">返回 500</button>
        <button class="secondary" @click="run('返回 503', () => api.status(503))">返回 503</button>
      </div>
    </div>

    <div class="tool-panel">
      <h3>异常接口</h3>
      <div class="actions">
        <button class="danger" @click="run('抛出服务器异常', () => api.error())">抛出服务器异常</button>
      </div>
    </div>

    <article class="tool-panel">
      <h3>诊断结果</h3>
      <pre v-if="result">{{ result }}</pre>
      <p v-else class="empty">执行一次诊断后显示 HTTP 状态、总耗时、后端实例和 traceId。</p>
    </article>
  </section>
</template>
