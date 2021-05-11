import { Message } from 'element-ui'
// import {showdown} from 'showdown'
// import {TurndownService} from 'turndown'
import showdownKatex from 'showdown-katex'

/** **********************************************************/
/**
 *  全局状态码
 */
const ECode = {
  // 默认页大小
  SUCCESS: "success",
  // 默认页码
  ERROR: "error",
}

/**
 * 全局配置文件
 */
const SysConf = {
  defaultAvatar: "https://gitee.com/moxi159753/wx_picture/raw/master/picture/favicon.png", // 默认头像
}

/** **********************************************************/

/**
 * 通用工具类
 */
const FUNCTIONS = {

  /**
   * 标签转字符串
   * @param tags
   * @returns {string}
   */
  tagsToString: tags => {
    let str = ''
    for (let i = 0; i < tags.length; i++) {
      str += tags[i] + ','
    }
    return str.substr(0, str.length - 1)
  },
  // 切割字符串
  splitStr: (str, flagCount) => {
    if (str == null || str == '') {
      return ""
    } else if(str.length > flagCount) {
      return str.substring(0, flagCount) + "..."
    } else {
      return str
    }
  },
  /**
   * 切割字符串
   * @param str
   * @param count
   * @returns {string|*}
   */
  strSubstring: (str, count) => {
    if (str == null || str == '') {
      return ""
    } else if(str.length > count) {
      return str.substring(0, count) + "..."
    } else {
      return str
    }
  },
  /**
   * 复制文字到剪切板
   * @param text
   */
  copyText: text => {
    const oInput = document.createElement('input')
    oInput.value = text
    document.body.appendChild(oInput)
    oInput.select() // 选择对象
    document.execCommand('Copy') // 执行浏览器复制命令
    oInput.className = 'oInput'
    oInput.style.display = 'none'
  },
  /**
   * 将Markdown转成Html
   * @param text
   */
  markdownToHtml: text => {
    let converter = new showdown.Converter({
      tables: true,
      extensions: [
        showdownKatex({
          // maybe you want katex to throwOnError
          throwOnError: true,
          // disable displayMode
          displayMode: false,
          // change errorColor to blue
          errorColor: '#1500ff',
        }),
      ],
    });
    let html = converter.makeHtml(text)
    return html;
  },
  /**
   * 将Html转成Markdown
   * @param text
   */
  htmlToMarkdown: text => {
    var turndownService = new TurndownService()

    // 用于提取代码语言
    turndownService.addRule('CodeBlock', {
      filter: function (node, options) {
        return (
          node.nodeName === 'PRE' &&
          node.firstChild &&
          node.firstChild.nodeName === 'CODE'
        )
      },
      replacement: function (content, node, options) {
        var className = node.firstChild.getAttribute('class') || ''
        var language = (className.match(/language-(\S+)/) || [null, ''])[1]
        return (
          '\n\n' + options.fence + language + '\n' +
          node.firstChild.textContent +options.fence
        )
      }
    })

    // 提取数学公式进行转换
    turndownService.addRule('multiplemath', {
      filter (node, options) {
        return node.classList.contains('vditor-math')
      },
      replacement (content, node, options) {
        console.log("中间内容", node.firstChild.textContent)
        return `$$ \n${node.firstChild.textContent}\n $$`
      }
    })

    var turndownPluginGfm = require('turndown-plugin-gfm')
    var gfm = turndownPluginGfm.gfm
    var tables = turndownPluginGfm.tables
    var strikethrough = turndownPluginGfm.strikethrough
    turndownService.use(gfm)
    turndownService.use([tables, strikethrough])

    console.log("转换后", turndownService.turndown(text))
    return turndownService.turndown(text)
  },
  /**
   * 将Html转成Markdown文件
   * @param title：标题
   * @param text：正文
   */
  htmlToMarkdownFile: (title, text) => {

    title = title || "默认标题"

    let turndownService = new TurndownService()

    let markdown = turndownService.turndown(text)

    //创建一个blob对象,file的一种
    let blob = new Blob([markdown])

    let link = document.createElement('a')

    link.href = window.URL.createObjectURL(blob)

    //配置下载的文件名
    link.download = title + '.md'

    link.click()
  },
  deepClone(obj,hash=new WeakMap()){
    if(obj==null) return obj;   //如果是null 或者undefined 我就不进行拷贝操作
    if(obj instanceof Date) return new Date(obj);
    if(obj instanceof RegExp) return new RegExp(obj);
    //可能是对象 或者普通的值 如果是函数的话，是不需要深拷贝的  因为函数是用来调用的，不需要再拷贝一个新的函数
    if(typeof obj!=='object') return obj;
    // 是对象的话就要进行深拷贝 [] {} Object.prototype.toString.call(obj)==[object Array]?[]:{}
    if(hash.get(obj)) return hash.get(obj);

    let cloneObj=new obj.constructor;
    hash.set(obj,cloneObj);
    for(let key in obj){
      if(obj.hasOwnProperty(key)){
        //实现一个递归拷贝
        cloneObj[key]= this.deepClone(obj[key],hash);
      }
    }
    return cloneObj;
  },
  /**
   * 通用提示信息
   * @type {{success: message.success, warning: message.warning, error: message.error, info: message.info}}
   */
  message: {
    success: function(message) {
      Message({
        showClose: true,
        message: message || '成功',
        type: 'success'
      })
    },
    warning: function(message) {
      Message({
        showClose: true,
        message: message || '警告',
        type: 'warning'
      })
    },
    info: function(message) {
      Message({
        showClose: true,
        message: message || '提示'
      })
    },
    error: function(message) {
      Message({
        showClose: true,
        message: message || '异常',
        type: 'error'
      })
    }
  }
}

export default {
  ECode,
  SysConf,
  FUNCTIONS
}
