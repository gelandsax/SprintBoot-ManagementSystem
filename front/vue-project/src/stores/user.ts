import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { User } from '../models'
export const useCurUserStore = defineStore('currentUser', () => {
  const curUser = ref<User|null>(null)
  function setCurUser(user:User){
    curUser.value = user
  }
  function userLogOut(){
    curUser.value = null
    localStorage.removeItem('curUser')

  }
  return { curUser,setCurUser,userLogOut }
},{
  persist: true
})
