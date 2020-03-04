
<!--
var ETNGpager = function( srcName, dstName, cntPP, cntPS )
{
this.srcName = srcName;
this.dstName = dstName;
this.curP   = 1;//默认当前页为第一页
this.cntPP   = cntPP || 2;//默认每页两条纪录
this.cntPS   = cntPS || 3;//默认每页显示5个分页上下文
this.items   = [];
this.showPNP = true;/*显示上下页链接*/
this.showType = true;/*滑动分页*/
this.result   = {pagedata:[],pagebar:'',limit:[0,0],report:''};
this.parse();/*总纪录数*/
}
ETNGpager.prototype.page = function (){
this.cntP = Math.ceil(this.cntR/this.cntPP);/*总页数*/
this.cntS = Math.ceil(this.cntP/this.cntPS);/*总段数*/
this.curS = Math.ceil(this.curP/this.cntPS);/*当前段*/
this.preP = this.curP -1;/*上一页*/
this.nextP = this.curP +1;/*下一页*/
this.preS = this.curS -1;/*上一段*/
this.nextS = this.curS +1;/*下一段*/
this.startR = (this.curP -1)*this.cntPP + 1;/*起始纪录*/
this.endR = (this.curP*this.cntPP >this.cntR)?this.cntR:this.curP*this.cntPP;/*结束纪录*/
this.result['pagedata']=[];
if(this.showType){
this.perSide = Math.floor(this.cntPS/2);
this.startP   = (this.curP > this.perSide)?(this.curP - this.perSide):1;
this.endP   = (this.startP + this.cntPS)>this.cntP?this.cntP:(this.startP + this.cntPS);
}else{
this.startP   = (this.curS-1)*this.cntPS+1;
this.endP   = (this.curS*this.cntPS>this.cntP)?this.cntP:(this.curS*this.cntPS);
}
for(var i = this.startP;i<=this.endP;i++){
this.result['pagedata'].push((i==this.curP)?'<a  href="#" class="curPage">'+i+'</a>':'<a  href="#" onclick="page('+i+')">'+i+'</a>');
}
if(this.showPNP){
if(this.curP>1)this.result['pagedata'].unshift('<a  href="#" onclick="page('+(this.curP-1)+')">上一页</a>');
if(this.curP<this.cntP)this.result['pagedata'].push('<a  href="#" onclick="page('+(this.curP+1)+')">下一页</a>');
}
this.result['pagebar'] = this.result['pagedata'].join('&nbsp;&nbsp;');
this.result['limit'] = [this.startR,this.endR];
this.result['report'] ='<a class="allpage"><b>'+this.cntR+'</b></a>&nbsp;&nbsp;' ;
}
ETNGpager.prototype.parse = function (){
var obj = document.getElementById(this.srcName);
for(var i = 0;i<obj.childNodes.length;i++){
if(obj.childNodes[i].nodeType!=3)this.items[this.items.length]=obj.childNodes[i].innerHTML;
}
this.cntR = this.items.length;
return this.items.length;
}
ETNGpager.prototype.create=function(){
this.page();
document.getElementById(this.dstName).innerHTML='<li>'+this.items.slice(this.startR-1,this.endR).join('</li><li>')+'</li>';
document.getElementById(this.dstName).innerHTML+='<div class="pagelist">'+this.result['report']+this.result['pagebar']+'</div>';
}
//-->
