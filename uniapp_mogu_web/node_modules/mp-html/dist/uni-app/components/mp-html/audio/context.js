const ctx = {}

export default {
  get: id => ctx[id],
  set: (id, vm) => { ctx[id] = vm },
  remove: id => { ctx[id] = undefined }
}
