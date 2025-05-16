<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useAuthStore} from "../../stores/auth.ts";
import {useRouter} from "vue-router";
import {toast} from "vue3-toastify";
import api from "../../services/api.ts";
import {Testimonial} from "../../types";
import {defaultAvatar} from "../../utils/constants.ts";



const testimonials = ref<Testimonial[]>([])
onMounted(async () => {
 await api.get<Testimonial[]>("/api/v1/reviews")
      .then((response) => {
        testimonials.value = response.data
      })
})

const showModal = ref(false)
const authStore = useAuthStore()
const router = useRouter()

const hoverRating = ref(0)

const newTestimonial = ref({
  name: '',
  quote: '',
  fullName: "",
  userProfileUrl: null as string | null ,
  rating: undefined as number | undefined
})
const fullName = ref<string>("")
const userProfileUrl = ref<string | null>("")

const setRating = (star: number) => {
  newTestimonial.value.rating = star
}

const openTestimonialModal = () => {
  if(!authStore.isAuthenticated) {
    router.push("/auth/login")
  }
  showModal.value = true

  if(authStore.user != null) {
    fullName.value = authStore.user.fullName
    userProfileUrl.value = authStore.user.userProfilePictureUrl ?? null
  } else {
    fullName.value = authStore.serviceProvider?.fullName ?? ""
    userProfileUrl.value = authStore.serviceProvider?.userProfilePictureUrl ?? null
  }
  newTestimonial.value.name = fullName.value
}

const submitTestimonial = async () => {
  if (!newTestimonial.value.quote.trim()) {
    toast.error('Please enter a testimonial.')
    return
  }

  await api.post("/api/v1/reviews", {
    quote: newTestimonial.value.quote,
    rating: newTestimonial.value.rating,
    fullName: fullName.value,
    userProfileUrl: userProfileUrl.value
  })
      .then(() => {
        toast.success("Successfully added")
          }
      )

  // Reset form and close modal
  newTestimonial.value = {name: '', quote: '', rating: undefined, fullName: fullName.value, userProfileUrl: userProfileUrl.value}
  showModal.value = false
}
</script>
<template>
  <section class="py-16 bg-neutral-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- Heading -->
      <div class="text-center mb-12">
        <h2 class="text-3xl font-bold text-neutral-900">What Our Users Say</h2>
        <p class="mt-4 text-xl text-neutral-600 max-w-2xl mx-auto">
          Don't take our word for it. Here's what our community has to say about RoadPal.
        </p>
      </div>

      <!-- Testimonials Grid -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div
            v-for="(testimonial, index) in testimonials"
            :key="index"
            class="bg-white p-6 rounded-lg shadow-md transition-all duration-300 hover:shadow-lg"
        >
          <div class="flex items-center mb-4">
            <img
                :src="testimonial.userProfileUrl ?? defaultAvatar"
                :alt="testimonial.fullName"
                class="w-12 h-12 rounded-full object-cover mr-4 border-2 border-primary-100"
            >
            <div>
              <h4 class="font-semibold text-neutral-900">{{ testimonial.fullName }}</h4>
              <p class="text-sm text-neutral-500">{{ testimonial.fullName }}</p>
            </div>
          </div>
          <blockquote class="text-neutral-700 italic">
            "{{ testimonial.quote }}"
          </blockquote>
          <!-- Rating Stars -->
          <div class="mt-4 flex">
            <svg
                v-for="i in 5"
                :key="i"
                :class="i <= testimonial.rating ? 'text-yellow-400' : 'text-gray-300'"
                class="w-5 h-5"
                fill="currentColor"
                viewBox="0 0 20 20"
            >
              <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0
         1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755
         1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0
         00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0
         00.951-.69l1.07-3.292z"
              />
            </svg>
          </div>
        </div>
      </div>

      <!-- Add Testimonial Button -->
      <div class="mt-12 text-center">
        <button
            @click="openTestimonialModal"
            class="inline-flex items-center px-6 py-3 bg-blue-600 text-white text-base font-semibold rounded-md shadow hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-offset-2 transition"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
          </svg>
          Add Testimonial
        </button>
      </div>
    </div>

    <!-- Testimonial Modal -->
    <div
        v-if="showModal"
        class="fixed inset-0 z-50 bg-black bg-opacity-50 flex items-center justify-center px-4"
    >
      <div class="bg-white w-full max-w-md rounded-lg shadow-lg p-6 relative">
        <h3 class="text-xl font-semibold text-gray-900 mb-4">Add Your Testimonial</h3>

        <label class="block text-sm font-medium text-gray-700 mb-1">Your Name</label>
        <input
            disabled
            v-model="newTestimonial.name"
            type="text"
            class="w-full mb-4 px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring focus:ring-blue-200"
            placeholder="John Doe"
        />

        <label class="block text-sm font-medium text-gray-700 mb-1">Your Rating</label>
        <div class="flex mb-4">
          <template v-for="star in [1, 2, 3, 4, 5]" :key="star">
            <svg
                @click="setRating(star)"
                @mouseover="hoverRating = star"
                @mouseleave="hoverRating = 0"
                :class="[
        'w-6 h-6 cursor-pointer transition-colors',
        star <= (hoverRating || newTestimonial.rating || 0) ? 'text-yellow-400' : 'text-gray-300'
      ]"
                fill="currentColor"
                viewBox="0 0 20 20"
                xmlns="http://www.w3.org/2000/svg"
            >
              <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.18 3.626a1 1 0 00.95.69h3.813c.969 0 1.371 1.24.588 1.81l-3.087 2.244a1 1 0 00-.364 1.118l1.18 3.626c.3.921-.755 1.688-1.54 1.118L10 13.347l-3.087 2.244c-.784.57-1.838-.197-1.539-1.118l1.18-3.626a1 1 0 00-.364-1.118L3.103 9.053c-.783-.57-.38-1.81.588-1.81h3.813a1 1 0 00.95-.69l1.18-3.626z"
              />
            </svg>
          </template>
        </div>

        <label class="block text-sm font-medium text-gray-700 mb-1">Your Review</label>
        <textarea
            v-model="newTestimonial.quote"
            class="w-full h-24 mb-4 px-3 py-2 border rounded-md shadow-sm resize-none focus:outline-none focus:ring focus:ring-blue-200"
            placeholder="Write your thoughts..."
        ></textarea>

        <div class="flex items-center justify-between space-x-4">
          <button
              @click="submitTestimonial"
              class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition"
          >
            Submit
          </button>
          <button
              @click="showModal = false"
              class="w-full bg-gray-200 text-gray-800 py-2 rounded-md hover:bg-gray-300 transition"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  </section>
</template>
