<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center px-4">
    <div class="text-center">
      <h1 class="text-6xl font-bold text-gray-900 mb-4">404</h1>
      <p class="text-xl text-gray-600 mb-8">Page not found</p>
      <router-link 
        to="/" 
        class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
      >
        Return Home
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
// No additional logic needed for this component
</script>