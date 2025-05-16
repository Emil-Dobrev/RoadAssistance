<script setup lang="ts">
import { useRouter } from 'vue-router'
import Button from '../ui/Button.vue'

const router = useRouter()

const navigateToSignUp = () => {
  router.push('/auth/register')
}

const navigateToRequestHelp = () => {
  router.push('/user/request')
}
</script>

<template>
  <section class="relative bg-gradient-to-br from-primary-600 to-primary-800 text-white py-20">
    <div class="absolute inset-0 bg-black opacity-40"></div>
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-10 items-center">
        <div class="space-y-8">
          <h1 class="text-4xl md:text-5xl font-bold leading-tight">
            Roadside Assistance When You Need It Most
          </h1>
          <p class="text-xl text-white/90 max-w-lg">
            Connect with trusted service providers in minutes. Help is just a tap away.
          </p>
          <div class="flex flex-col sm:flex-row space-y-4 sm:space-y-0 sm:space-x-4">
            <Button 
              variant="accent" 
              size="lg" 
              @click="navigateToSignUp"
              class="sm:w-auto"
            >
              Sign Up Now
            </Button>
            <Button 
              variant="secondary" 
              size="lg" 
              @click="navigateToRequestHelp"
              class="sm:w-auto"
            >
              Request Help
            </Button>
          </div>
        </div>
        <div class="hidden md:block">
          <img 
            src="https://images.pexels.com/photos/2952546/pexels-photo-2952546.jpeg?auto=compress&cs=tinysrgb&w=800" 
            alt="Roadside assistance" 
            class="rounded-lg shadow-xl max-h-96 object-cover w-full"
          />
        </div>
      </div>
    </div>
  </section>
</template>