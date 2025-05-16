<template>
  <div class="max-w-5xl mx-auto p-6 space-y-8">
    <!-- Profile Header -->
    <div
        class="flex flex-col sm:flex-row items-center sm:items-start space-y-4 sm:space-y-0 sm:space-x-6 bg-white p-6 rounded-2xl shadow-lg">
      <!-- Profile Picture -->
      <img
          :src="provider?.avatarUrl || defaultAvatar"
          alt="Profile Picture"
          class="w-28 h-28 rounded-full object-cover border-4 border-white shadow-md"
      />

      <!-- Info -->
      <div class="text-center sm:text-left">
        <!-- Full Name -->
        <h2 class="text-2xl font-bold text-gray-800">
          {{ provider?.firstName }} {{ provider?.lastName }}
        </h2>
        <!-- Company Name -->
        <p class="text-gray-500 text-lg">{{ provider?.companyName }}</p>

        <!-- Rating & Reviews -->
        <div class="flex items-center justify-center sm:justify-start space-x-2 mt-2">
          <!-- Stars -->
          <div class="flex items-center space-x-1">
            <template v-for="n in 5" :key="n">
              <svg
                  :class="[
      'w-5 h-5',
      isViewMode ? 'cursor-pointer' : 'cursor-default',
      n <= hoverRating || (!hoverRating && n <= Math.floor(provider?.rating || 0))
        ? 'text-yellow-400'
        : 'text-gray-300'
    ]"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                  @click="isViewMode && handleStarClick(n)"
                  @mouseover="isViewMode && (hoverRating = n)"
                  @mouseleave="isViewMode && (hoverRating = 0)"
              >
                <path
                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.967a1 1 0 00.95.69h4.178c.969 0 1.371 1.24.588 1.81l-3.382 2.455a1 1 0 00-.364 1.118l1.286 3.966c.3.921-.755 1.688-1.54 1.118l-3.382-2.455a1 1 0 00-1.175 0l-3.382 2.455c-.784.57-1.838-.197-1.54-1.118l1.286-3.966a1 1 0 00-.364-1.118L2.07 9.394c-.783-.57-.38-1.81.588-1.81h4.178a1 1 0 00.95-.69l1.286-3.967z"/>
              </svg>
            </template>
          </div>
          <!-- Reviews Count -->
          <span class="text-sm text-gray-400">
            ({{ provider?.reviewCount ?? 0 }} reviews)
          </span>
        </div>
      </div>
    </div>

    <!-- Editable Fields -->
    <div class="space-y-6">
      <!-- Company Description -->
      <div class="bg-white rounded-2xl shadow p-6 hover:shadow-md transition-shadow">
        <h4 class="text-lg font-semibold mb-2 text-gray-800">Company Description</h4>
        <div>
          <template v-if="isViewMode">
            <p class="text-gray-700">{{ editableProvider?.companyDescription }}</p>
          </template>
          <template v-else>
            <textarea v-model="editableProvider.companyDescription" class="w-full border rounded-lg p-3"></textarea>
          </template>
        </div>
      </div>

      <!-- Phone Number -->
      <div class="bg-white rounded-2xl shadow p-6 hover:shadow-md transition-shadow">
        <h4 class="text-lg font-semibold mb-2 text-gray-800">Phone Number</h4>
        <div>
          <template v-if="isViewMode">
            <div class="flex items-center">
              <p class="text-gray-700 flex-1">
                {{ showPhoneNumber ? editableProvider?.phoneNumber : '•••••••••••' }}
              </p>

              <button
                  v-if="!showPhoneNumber"
                  @click="togglePhoneNumber"
                  class="ml-4 text-blue-500 hover:text-blue-700"
              >
                Show Number
              </button>

              <!-- Copy icon placed at the end of the phone number -->
              <button
                  v-if="showPhoneNumber"
                  @click="copyToClipboard(editableProvider?.phoneNumber)"
                  class="ml-4 text-blue-500 hover:text-blue-700"
                  title="Copy"
              >
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" class="w-5 h-5">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7h6M13 3h6a2 2 0 012 2v12a2 2 0 01-2 2H7a2 2 0 01-2-2V7a2 2 0 012-2h6M13 7V3m0 4V3m-6 4H3m0 4h6M3 7V3m6 4V3m0 16a2 2 0 001-1V8a2 2 0 00-2-2h-1m2 2a2 2 0 01-1 1z"></path>
                </svg>
              </button>
            </div>
          </template>

          <template v-else>
            <input v-model="editableProvider.phoneNumber" type="tel" class="w-full border rounded-lg p-3"
                   placeholder="Enter your phone number"/>
          </template>
        </div>
      </div>

      <!-- Address -->
      <div class="bg-white rounded-2xl shadow p-6 hover:shadow-md transition-shadow">
        <h4 class="text-lg font-semibold mb-2 text-gray-800">Address</h4>
        <div>
          <template v-if="isViewMode">
            <p class="text-gray-700">{{ editableProvider?.address }}</p>
          </template>
          <template v-else>
            <input v-model="editableProvider.address" type="text" class="w-full border rounded-lg p-3"
                   placeholder="Enter your address"/>
          </template>
        </div>
      </div>

      <!-- Non-Stop Service -->
      <div class="bg-white rounded-2xl shadow p-6 hover:shadow-md transition-shadow">
        <h4 class="text-lg font-semibold mb-2 text-gray-800">Service Hours</h4>
        <div>
          <div v-if="isViewMode">
            <span class="text-green-600 font-semibold">
              {{ editableProvider?.isNonStop ? '24/7 Available' : '' }}
            </span>
          </div>
          <div v-else class="flex items-center space-x-3">
            <input type="checkbox" v-model="editableProvider.isNonStop"/>
            <span class="font-medium">Non-Stop (24/7 Service)</span>
          </div>
        </div>
      </div>

      <!-- Save Button -->
      <div v-if="!isViewMode" class="text-center">
        <button @click="saveChanges" class="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 transition">
          Save Changes
        </button>
      </div>
    </div>

    <!-- Comments -->
    <div v-if="provider?.comments" class="space-y-4">
      <h3 class="text-2xl font-bold text-gray-800">Customer Reviews</h3>
      <div v-if="provider.comments.length" class="space-y-4">
        <div v-for="comment in provider.comments" :key="comment.id"
             class="bg-white rounded-2xl shadow p-6 hover:shadow-md transition-shadow">
          <div class="flex items-center space-x-3 mb-2">
            <img
                :src="comment.author.userProfilePictureUrl || defaultAvatar"
                alt="Avatar"
                class="w-10 h-10 rounded-full object-cover"
            />
            <div>
              <p class="font-medium">{{ comment.author.userFullName }}</p>
            </div>
          </div>
          <p class="text-gray-700">{{ comment.message }}</p>
          <p class="text-sm text-gray-400 mt-1">{{ new Date(comment.createdAt).toLocaleDateString() }}</p>
        </div>
      </div>
      <p v-else class="text-gray-500">No reviews yet.</p>
      <!--  Comment Box -->
      <div v-if="canWriteComments" class="mt-6">
        <h4 class="text-lg font-semibold text-gray-800 mb-2">Leave a Review</h4>
        <textarea
            v-model="newComment"
            class="w-full border border-gray-300 rounded-lg p-3 focus:outline-none focus:ring focus:border-blue-400"
            rows="4"
            placeholder="Write your comment here..."
        ></textarea>

        <button
            @click="submitComment"
            class="mt-2 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
        >
          Submit
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import {CommentRequest, ServiceProvider, ServiceProviderUpdateDTO} from '../../types'
import {useRoute} from "vue-router"
import {useAuthStore} from "../../stores/auth.ts"
import {getServiceProviderById, postComment, setProviderRating, updateProvider} from "../../services/providerService.ts"
import {copyToClipboard} from "../../utils/copyUtil.ts";
import {defaultAvatar} from "../../utils/constants.ts";
import api from "../../services/api.ts";

const route = useRoute()
const authStore = useAuthStore()


const providerId = route.params.providerId as string
const isViewMode = ref(true)
const isLoading = ref(false)
const showPhoneNumber = ref(false)
const provider = ref<ServiceProvider | null>(null)
const editableProvider = ref<ServiceProvider | null>(null)
const canWriteComments = ref<boolean>(false)

const saveChanges = async () => {
  await updateProvider(providerId, {
    companyDescription: editableProvider.value?.companyDescription ?? "",
    address: editableProvider.value?.address ?? "",
    phoneNumber: editableProvider.value?.phoneNumber ?? "",
    isNonStop: editableProvider.value?.isNonStop,
  }as ServiceProviderUpdateDTO)
      .then(
          (response) => {
            provider.value = response.data
            editableProvider.value = response.data
          })
      .catch(() => {
        //TODO add toast that it fail to update
      })
}

function togglePhoneNumber() {
  showPhoneNumber.value = true // Once shown, prevent hiding again
  api.post("/api/v1/providers/stats", {providerId: providerId})
}

const hoverRating = ref(0)

const handleStarClick = (rating: number) => {
  if (isViewMode.value) {
    submitRating(rating)
  }
}

const submitRating = async (rating: number) => {
  try {
    await setProviderRating({
      providerId: provider.value?.id ?? "",
      rating
    })
    //TODO show toast
    provider.value = await getServiceProviderById(providerId)
    editableProvider.value = provider.value ? {...provider.value} : null

  } catch (error) {
    //TODO show toast
  }
}

const newComment = ref('')

async function submitComment() {
  if (!newComment.value.trim()) return

  const comment: CommentRequest = {
    message: newComment.value,
    author: {
      userId: authStore.user?.id ?? "",
      userFullName: authStore.user?.fullName ?? "",
      userProfilePictureUrl: authStore.user?.avatarUrl ?? "" // Replace with real user data
    }
  }
  console.log(comment)
  await postComment(providerId, comment)
      .then(async () => provider.value = await getServiceProviderById(providerId))
      .catch(() =>
          // TODO add toast message
          {}
      )

  newComment.value = '' // Clear textarea
}

onMounted(async () => {
  isLoading.value = true
  // only users can write comments
  if (authStore.user != null) {
    canWriteComments.value = true
  }

  try {
    provider.value = await getServiceProviderById(providerId)
    editableProvider.value = provider.value ? {...provider.value} : null
    if (authStore.serviceProvider && authStore.serviceProvider.id === providerId) {
      isViewMode.value = false
    }
  } finally {
    isLoading.value = false
  }
})
</script>
