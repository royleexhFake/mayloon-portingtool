﻿$_L(["java.lang.Thread"],"java.lang.ref.Reference",["java.lang.ref.ReferenceQueue"],function(){
c$=$_C(function(){
this.referent=null;
this.queue=null;
this.next=null;
this.discovered=null;
$_Z(this,arguments);
},java.lang.ref,"Reference");
$_M(c$,"get",
function(){
return this.referent;
});
$_M(c$,"clear",
function(){
this.referent=null;
});
$_M(c$,"isEnqueued",
function(){
{
return(this.queue!==java.lang.ref.ReferenceQueue.NULL)&&(this.next!=null);
}});
$_M(c$,"enqueue",
function(){
return this.queue.enqueue(this);
});
$_K(c$,
function(referent){
this.construct(referent,null);
},"~O");
$_K(c$,
function(referent,queue){
this.referent=referent;
this.queue=(queue==null)?java.lang.ref.ReferenceQueue.NULL:queue;
},"~O,java.lang.ref.ReferenceQueue");
$_H();
c$=$_T(java.lang.ref.Reference,"Lock");
c$=$_P();
$_H();
c$=$_T(java.lang.ref.Reference,"ReferenceHandler",Thread);
$_V(c$,"run",
function(){
for(;;){
var r;
{
if(java.lang.ref.Reference.pending!=null){
r=java.lang.ref.Reference.pending;
var rn=r.next;
($t$=java.lang.ref.Reference.pending=(rn===r)?null:rn,java.lang.ref.Reference.prototype.pending=java.lang.ref.Reference.pending,$t$);
r.next=r;
}else{
try{
java.lang.ref.Reference.lock.wait();
}catch(x){
if($_O(x,InterruptedException)){
}else{
throw x;
}
}
continue;}}if($_O(r,sun.misc.Cleaner)){
(r).clean();
continue;}var q=r.queue;
if(q!==java.lang.ref.ReferenceQueue.NULL)q.enqueue(r);
}
});
c$=$_P();
c$.lock=c$.prototype.lock=new java.lang.ref.Reference.Lock();
$_S(c$,
"pending",null);
{
if(false){
var tg=Thread.currentThread().getThreadGroup();
for(var tgn=tg;tgn!=null;tg=tgn,tgn=tg.getParent());
var handler=new java.lang.ref.Reference.ReferenceHandler(tg,"Reference Handler");
handler.setPriority(10);
handler.start();
}}});
