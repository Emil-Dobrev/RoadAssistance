<template>
  <div class="bg-white rounded-2xl shadow p-6 hover:shadow-md transition-shadow">
    <h4 class="text-lg font-semibold mb-2 text-gray-800">{{ label }}</h4>
    <div>
      <template v-if="isViewMode">
        <p class="text-gray-700">{{ value }}</p>
      </template>
      <template v-else>
        <textarea
            v-if="type === 'textarea'"
            :value="value"
            @input="$emit('update', $event.target?.value)"
            class="w-full border rounded-lg p-3"
        ></textarea>
        <input
            v-else
            :type="inputType"
            :value="value"
            @input="$emit('update', $event.target.value)"
            :placeholder="placeholder"
            class="w-full border rounded-lg p-3"
        />
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  label: string
  value: string | number | undefined
  isViewMode: boolean
  type: 'input' | 'textarea'
  inputType?: string
  placeholder?: string
}>()

defineEmits(['update'])
</script>
