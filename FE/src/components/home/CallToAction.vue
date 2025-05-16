<script setup lang="ts">
import { useRouter } from 'vue-router'
import Button from '../ui/Button.vue'

const router = useRouter()

const navigateToSignUp = () => {
  router.push('/auth/register')
}

const navigateToProviderSignUp = () => {
  router.push('/auth/register')
}
</script>

<template>
  <section class="py-16 bg-primary-600 text-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-8">
        <h2 class="text-3xl font-bold">Ready to Join RoadPal?</h2>
        <p class="mt-4 text-xl text-white/80 max-w-2xl mx-auto">
          Whether you need help on the road or want to provide services, RoadPal connects you.
        </p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8 mt-12">
        <div class="bg-white/10 backdrop-blur-sm p-8 rounded-lg text-center">
          <h3 class="text-2xl font-semibold mb-4">Need Road Assistance?</h3>
          <p class="mb-6 text-white/90">
            Sign up as a user to request help whenever you need it. Connect with nearby service providers in minutes.
          </p>
          <Button 
            variant="accent" 
            size="lg" 
            @click="navigateToSignUp"
          >
            Sign Up as User
          </Button>
        </div>
        
        <div class="bg-white/10 backdrop-blur-sm p-8 rounded-lg text-center">
          <h3 class="text-2xl font-semibold mb-4">Provide Road Services?</h3>
          <p class="mb-6 text-white/90">
            Join our network of service providers. Get connected to users in your area who need your services.
          </p>
          <Button 
            variant="secondary" 
            size="lg" 
            @click="navigateToProviderSignUp"
          >
            Sign Up as Provider
          </Button>
        </div>
      </div>
    </div>
  </section>
</template>