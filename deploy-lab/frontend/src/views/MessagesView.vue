<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { api, toApiError } from '../api'
import type { Message } from '../types'

const messages = ref<Message[]>([])
const title = ref('')
const content = ref('')
const expiresAt = ref('')
const error = ref('')
const loading = ref(false)

async function loadMessages() {
  try {
    messages.value = (await api.listMessages()).data
  } catch (err) {
    error.value = toApiError(err).message
  }
}

async function createMessage() {
  loading.value = true
  error.value = ''
  try {
    await api.createMessage({
      title: title.value,
      content: content.value,
      expiresAt: expiresAt.value ? new Date(expiresAt.value).toISOString() : null
    })
    title.value = ''
    content.value = ''
    expiresAt.value = ''
    await loadMessages()
  } catch (err) {
    error.value = toApiError(err).message
  } finally {
    loading.value = false
  }
}

async function deleteMessage(id: number) {
  await api.deleteMessage(id)
  await loadMessages()
}

async function clearMessages() {
  await api.clearMessages()
  await loadMessages()
}

onMounted(loadMessages)
</script>

<template>
  <section class="page">
    <div class="section-head">
      <div>
        <h2>临时消息</h2>
        <p class="meta">当前数据仅保存在后端内存中，应用重启后将全部清空。</p>
      </div>
      <button class="secondary" @click="loadMessages">刷新</button>
    </div>

    <p v-if="error" class="notice error">{{ error }}</p>

    <form class="tool-panel" @submit.prevent="createMessage">
      <div class="form-grid">
        <label class="field">
          <span>标题</span>
          <input v-model="title" maxlength="100" required />
        </label>
        <label class="field">
          <span>过期时间</span>
          <input v-model="expiresAt" type="datetime-local" />
        </label>
        <label class="field full">
          <span>内容</span>
          <textarea v-model="content" maxlength="2000" />
        </label>
      </div>
      <div class="actions">
        <button :disabled="loading">{{ loading ? '创建中' : '创建消息' }}</button>
        <button type="button" class="danger" @click="clearMessages">清空</button>
      </div>
    </form>

    <div class="message-list">
      <article v-for="message in messages" :key="message.id" class="message-item">
        <div class="section-head">
          <h3>{{ message.title }}</h3>
          <button class="secondary" @click="deleteMessage(message.id)">删除</button>
        </div>
        <p>{{ message.content || '无内容' }}</p>
        <span class="meta">创建 {{ message.createdAt }} · 过期 {{ message.expiresAt ?? '不自动过期' }}</span>
      </article>
      <p v-if="messages.length === 0" class="empty">还没有临时消息。</p>
    </div>
  </section>
</template>
