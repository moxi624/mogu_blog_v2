export default {
  inserted(el, binding) {
    el.style.cssText = 'cursor: pointer;color:write;'
    el.addEventListener('click', () => {
      let link = document.createElement('a')
      let url = binding.value
      // 这里是将url转成blob地址，
      fetch(url).then(res => res.blob()).then(blob => { // 将链接地址字符内容转变成blob地址
        link.href = URL.createObjectURL(blob)
        link.download = ''
        document.body.appendChild(link)
        link.click()
      })
    })
  }
}
