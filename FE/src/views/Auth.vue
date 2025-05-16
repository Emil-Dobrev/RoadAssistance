<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="min-h-screen bg-neutral-50 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div class="text-center">
        <router-link to="/" class="flex items-center justify-center mb-8">
          <span class="text-3xl font-bold text-primary-500">RoadPal</span>
        </router-link>
      </div>
      
      <router-view></router-view>
    </div>
  </div>
</template>