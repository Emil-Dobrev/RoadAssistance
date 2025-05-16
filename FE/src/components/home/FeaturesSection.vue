<script setup lang="ts">
const features = [
  {
    title: "Quick Response",
    description: "Connect with nearby service providers in minutes, not hours.",
    icon: "bolt"
  },
  {
    title: "Real-time Tracking",
    description: "Track your service provider's location as they head to you.",
    icon: "map"
  },
  {
    title: "Verified Providers",
    description: "Every service provider is vetted and rated by our community.",
    icon: "shield-check"
  },
  {
    title: "Transparent Pricing",
    description: "Know exactly what you'll pay before confirming service.",
    icon: "currency-dollar"
  },
  {
    title: "24/7 Availability",
    description: "Get help any time of day or night, even on holidays.",
    icon: "clock"
  },
  {
    title: "Comprehensive Services",
    description: "From flat tires to towing, battery jumps to lockouts, we've got you covered.",
    icon: "wrench"
  }
]
</script>

<template>
  <section class="py-16 bg-white">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-16">
        <h2 class="text-3xl font-bold text-neutral-900">Why Choose RoadPal?</h2>
        <p class="mt-4 text-xl text-neutral-600 max-w-2xl mx-auto">
          We're revolutionizing roadside assistance with technology that connects you directly with help.
        </p>
      </div>
      
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
        <div 
          v-for="(feature, index) in features" 
          :key="index"
          class="bg-neutral-50 p-6 rounded-lg shadow-sm border border-neutral-100 transition-transform duration-300 hover:transform hover:scale-105"
        >
          <div class="w-12 h-12 bg-primary-100 rounded-lg flex items-center justify-center mb-4">
            <svg class="w-6 h-6 text-primary-600" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
            </svg>
          </div>
          <h3 class="text-lg font-semibold text-neutral-900 mb-2">{{ feature.title }}</h3>
          <p class="text-neutral-600">{{ feature.description }}</p>
        </div>
      </div>
    </div>
  </section>
</template>