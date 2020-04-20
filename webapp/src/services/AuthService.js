const LOCAL_STORAGE_USER_KEY = 'user';

export function getUserFromPersistentStorage() {
  try {
    return JSON.parse(localStorage.getItem(LOCAL_STORAGE_USER_KEY));
  } catch(ex) {
    return null;
  }
}

export function persistAuthChanges(store) {
  let currentUser = null;
  function handleLocalStorageUserChange() {
    let previousUser = currentUser;
    currentUser = store.getState().user;
    if (previousUser !== currentUser) {
      localStorage.setItem(LOCAL_STORAGE_USER_KEY, JSON.stringify(currentUser));
    }
  }
  store.subscribe(handleLocalStorageUserChange);
}
