function highlight(el){
    let blocks = el.querySelectorAll('pre code');
    blocks.forEach((block) => {
      hljs.highlightBlock(block)
    })
}


export default{
    bind(el){
        highlight(el);
    }
}

