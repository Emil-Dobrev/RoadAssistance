import { createI18n } from 'vue-i18n'
import bg from './locales/bg'
import en from './locales/en'

export const i18n = createI18n({
  legacy: false,
  locale: 'bg', // Set Bulgarian as default
  fallbackLocale: 'en',
  messages: {
    bg,
    en
  }
})