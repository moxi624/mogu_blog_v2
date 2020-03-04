
<!--
var pager = new ETNGpager('list','list2',24,10);//24为每页显示条数。10为导航显示菜单数
var curP = 1;
page()
function page(i){
curP =(curP>pager.cntP)?1:curP;
if(i){
curP = n =i;
}else{
n = curP++;
}
pager.curP = (n>pager.cntP)?pager.cntP:n;
pager.create();
}
//-->
