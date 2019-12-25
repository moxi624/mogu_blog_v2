/**
 * @license Copyright (c) 2003-2018, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function (config) {
	// Define changes to default configuration here.
	// For complete reference see:
	// http://docs.ckeditor.com/#!/api/CKEDITOR.config

	// The toolbar groups arrangement, optimized for two toolbar rows.



	config.toolbarGroups = [
		{ name: 'clipboard', groups: ['clipboard', 'undo'] },
		{ name: 'editing', groups: ['find', 'selection', 'spellchecker'] },
		{ name: 'links' },
		{ name: 'insert' },
		{ name: 'forms' },
		{ name: 'tools' },
		{ name: 'document', groups: ['mode', 'document', 'doctools'] },
		{ name: 'others' },
		'/',
		{ name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
		{ name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi'] },
		{ name: 'styles' },
		{ name: 'colors' },
		{ name: 'about' }
	];

	// 配置disallowedContent，过滤掉宽高
	config.disallowedContent = 'img{width,height};img[width,height]';

	config.keystrokes = [
		[ CKEDITOR.ALT + 121 /*F10*/, 'toolbarFocus' ],  //获取焦点
		 [ CKEDITOR.ALT + 122 /*F11*/, 'elementsPathFocus' ],  //元素焦点
		[ CKEDITOR.SHIFT + 121 /*F10*/, 'contextMenu' ],  //文本菜单
		[ CKEDITOR.CTRL + 90 /*Z*/, 'undo' ],  //撤销
		 [ CKEDITOR.CTRL + 89 /*Y*/, 'redo' ],  //重做
		 [ CKEDITOR.CTRL + CKEDITOR.SHIFT + 90 /*Z*/, 'redo' ],  //
		 [ CKEDITOR.CTRL + 76 /*L*/, 'link' ],  //链接
		 [ CKEDITOR.CTRL + 66 /*B*/, 'bold' ],  //粗体
		 [ CKEDITOR.CTRL + 73 /*I*/, 'italic' ],  //斜体
		 [ CKEDITOR.CTRL + 85 /*U*/, 'underline' ],  //下划线
		 [ CKEDITOR.ALT + 109 /*-*/, 'toolbarCollapse' ]
	]

	//删除标准插件提供的一些按钮
	//标准工具栏中不需要。
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	//添加插件，多个插件用逗号隔开
	// codesnippet: 代码块   ， uploadimage: 上传图片,  colorbutton: 颜色按钮
	config.extraPlugins = 'codesnippet,panelbutton,floatpanel,colorbutton,markdown,colordialog,dialog,dialogui,smiley,widget,lineutils,eqneditor';

	//设置前景色的取值 plugins/colorbutton/plugin.js
	config.colorButton_colors = "000,800000,8B4513,2F4F4F,008080,000080,4B0082,696969,B22222,A52A2A,DAA520,006400,40E0D0,0000CD,800080,808080,F00,FF8C00,FFD700,008000,0FF,00F,EE82EE,A9A9A9,FFA07A,FFA500,FFFF00,00FF00,AFEEEE,ADD8E6,DDA0DD,D3D3D3,FFF0F5,FAEBD7,FFFFE0,F0FFF0,F0FFFF,F0F8FF,E6E6FA,FFF";

	//配置代码块风格
	codeSnippet_theme: 'zenburn';

    //使用zenburn的代码高亮风格样式 PS:zenburn效果就是黑色背景
    //如果不设置着默认风格为default

	//设置语言
	config.language = 'zh-cn';

	// 简化对话框窗口。
	config.removeDialogTabs = 'image:advanced;link:advanced';

	//开启工具栏“图像”中文件上传功能，后面的url为待会要上传action要指向的的action或servlet
	config.filebrowserImageUploadUrl = "http://101.132.194.128:8602/ckeditor/imgUpload?";

	//开启插入\编辑超链接中文件上传功能，后面的url为待会要上传action要指向的的action或servlet
	config.filebrowserUploadUrl = 'http://101.132.194.128:8602/ckeditor/fileUpload';

	//开启flash中文件上传，这里因为不常用，我暂时就不配置了
	//config.filebrowserFlashUploadUrl="";

	//工具栏“图像”中预览区域显示内容
	config.image_previewText = ' ';

	config.font_names = '宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;雅黑/雅黑;'+ config.font_names ;
};
