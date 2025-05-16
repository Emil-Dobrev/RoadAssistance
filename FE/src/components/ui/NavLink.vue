<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps({
  to: {
    type: String,
    required: true
  },
  exact: {
    type: Boolean,
    default: false
  },
  icon: {
    type: String,
    default: ''
  }
})

const route = useRoute()

const isActive = computed(() => {
  if (props.exact) {
    return route.path === props.to
  }
  return route.path.startsWith(props.to)
})
</script>

<template>
  <router-link
    :to="to"
    :class="[
      'flex items-center px-3 py-2 text-sm font-medium rounded-md transition-colors duration-150',
      isActive 
        ? 'text-primary-700 bg-primary-50' 
        : 'text-neutral-700 hover:text-primary-700 hover:bg-neutral-100'
    ]"
  >
    <slot></slot>
  </router-link>
</template>