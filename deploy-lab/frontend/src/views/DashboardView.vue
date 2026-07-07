<script setup lang="ts">
import { computed } from 'vue'
import type { SystemInfo } from '../types'

const props = defineProps<{
  systemInfo: SystemInfo | null
}>()

const uptime = computed(() => {
  const seconds = props.systemInfo?.uptimeSeconds ?? 0
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  return `${hours}h ${minutes}m ${seconds % 60}s`
})

function size(bytes?: number) {
  if (!bytes) {
    return '-'
  }
  return `${(bytes / 1024 / 1024).toFixed(1)} MB`
}
</script>

<template>
  <section class="page">
    <div class="section-head">
      <div>
        <h2>运行状态</h2>
        <p class="meta">每 10 秒刷新一次，适合验证实例切换、端口和环境变量。</p>
      </div>
    </div>

    <div class="grid">
      <article class="card">
        <span>应用版本</span>
        <strong>{{ systemInfo?.appVersion ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>实例编号</span>
        <strong>{{ systemInfo?.instanceId ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>运行环境</span>
        <strong>{{ systemInfo?.activeProfile ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>服务端口</span>
        <strong>{{ systemInfo?.serverPort ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>启动时间</span>
        <strong>{{ systemInfo?.startedAt ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>运行时长</span>
        <strong>{{ uptime }}</strong>
      </article>
      <article class="card">
        <span>Java 版本</span>
        <strong>{{ systemInfo?.javaVersion ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>操作系统</span>
        <strong>{{ systemInfo?.osName ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>主机名</span>
        <strong>{{ systemInfo?.hostname ?? '-' }}</strong>
      </article>
      <article class="card">
        <span>JVM 内存</span>
        <strong>{{ size(systemInfo?.usedMemoryBytes) }} / {{ size(systemInfo?.maxMemoryBytes) }}</strong>
      </article>
      <article class="card">
        <span>前端版本</span>
        <strong>1.0.0</strong>
      </article>
      <article class="card">
        <span>诊断接口</span>
        <strong>{{ systemInfo?.diagnosticsEnabled ? '已启用' : '已关闭' }}</strong>
      </article>
    </div>
  </section>
</template>
