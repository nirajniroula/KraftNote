const Editor = new Proxy({} , {
  get() {
    return console.log
  }
})
