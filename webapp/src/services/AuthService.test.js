import { getUserFromPersistentStorage, connectStoreToLocalStorage } from './AuthService';

describe('getUserFromPersistentStorage', () => {

  it('returns a user from localStorage', () => {
    localStorage.getItem.mockReturnValueOnce(JSON.stringify({ firstName: 'Alice' }));
    expect(getUserFromPersistentStorage()).toStrictEqual({ firstName: 'Alice'});
  });

  it('returns null when localStorage item does not exist', () => {
    localStorage.getItem.mockReturnValueOnce(JSON.stringify({ firstName: 'Alice' }));
    expect(getUserFromPersistentStorage()).toStrictEqual({ firstName: 'Alice'});
  });

  it('returns null when localStorage contains non-JSON', () => {
    localStorage.getItem.mockReturnValueOnce('carrot');
    expect(getUserFromPersistentStorage()).toBeNull();
  });

});

describe('connectStoreToLocalStorage', () => {

  it('sets an item to localStorage when the store publishes a user change', () => {
    let subscriber;
    const mockStore = {
      subscribe: f => subscriber = f,
      getState: () => { return { user: { firstName: 'Alice' } } }
    };

    connectStoreToLocalStorage(mockStore);

    subscriber(); // simulate store state change
    
    expect(localStorage.setItem.mock.calls.length).toBe(1);
    expect(localStorage.setItem.mock.calls[0][0]).toBe('user');
    expect(localStorage.setItem.mock.calls[0][1]).toBe(JSON.stringify({ firstName: 'Alice' }));
  });

});
