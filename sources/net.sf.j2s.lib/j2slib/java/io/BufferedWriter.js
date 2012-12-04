﻿$_L(["java.io.Writer"],"java.io.BufferedWriter",["java.io.IOException","java.lang.IllegalArgumentException","$.IndexOutOfBoundsException","$.NullPointerException","$.StringIndexOutOfBoundsException"],function(){
c$=$_C(function(){
this.out=null;
this.buf=null;
this.pos=0;
this.lineSeparator="\n";
$_Z(this,arguments);
},java.io,"BufferedWriter",java.io.Writer);
$_K(c$,
function(out){
this.construct(out,8192);
},"java.io.Writer");
$_K(c$,
function(out,size){
$_R(this,java.io.BufferedWriter,[out]);
if(size<=0){
throw new IllegalArgumentException("size<=0");
}this.out=out;
this.buf=$_A(size,'\0');
},"java.io.Writer,~N");
$_M(c$,"close",
function(){
{
if(this.isClosed()){
return;
}var thrown=null;
try{
this.flushInternal();
}catch(e){
thrown=e;
}
this.buf=null;
try{
this.out.close();
}catch(e){
if(thrown==null){
thrown=e;
}}
this.out=null;
if(thrown!=null){
}}});
$_M(c$,"flush",
function(){
{
this.checkNotClosed();
this.flushInternal();
this.out.flush();
}});
$_M(c$,"checkNotClosed",
($fz=function(){
if(this.isClosed()){
throw new java.io.IOException("BufferedWriter is closed");
}},$fz.isPrivate=true,$fz));
$_M(c$,"flushInternal",
($fz=function(){
if(this.pos>0){
this.out.write(this.buf,0,this.pos);
}this.pos=0;
},$fz.isPrivate=true,$fz));
$_M(c$,"isClosed",
($fz=function(){
return this.out==null;
},$fz.isPrivate=true,$fz));
$_M(c$,"newLine",
function(){
this.write("\n",0,"\n".length);
});
$_M(c$,"write",
function(cbuf,offset,count){
{
this.checkNotClosed();
if(cbuf==null){
throw new NullPointerException("buffer==null");
}if((offset|count)<0||offset>cbuf.length-count){
throw new IndexOutOfBoundsException();
}if(this.pos==0&&count>=this.buf.length){
this.out.write(cbuf,offset,count);
return;
}var available=this.buf.length-this.pos;
if(count<available){
available=count;
}if(available>0){
System.arraycopy(cbuf,offset,this.buf,this.pos,available);
this.pos+=available;
}if(this.pos==this.buf.length){
this.out.write(this.buf,0,this.buf.length);
this.pos=0;
if(count>available){
offset+=available;
available=count-available;
if(available>=this.buf.length){
this.out.write(cbuf,offset,available);
return;
}System.arraycopy(cbuf,offset,this.buf,this.pos,available);
this.pos+=available;
}}}},"~A,~N,~N");
$_M(c$,"write",
function(oneChar){
{
this.checkNotClosed();
if(this.pos>=this.buf.length){
this.out.write(this.buf,0,this.buf.length);
this.pos=0;
}this.buf[this.pos++]=String.fromCharCode(oneChar);
}},"~N");
$_M(c$,"write",
function(str,offset,count){
{
this.checkNotClosed();
if(count<=0){
return;
}if(offset>str.length-count||offset<0){
throw new StringIndexOutOfBoundsException();
}if(this.pos==0&&count>=this.buf.length){
var chars=$_A(count,'\0');
str.getChars(offset,offset+count,chars,0);
this.out.write(chars,0,count);
return;
}var available=this.buf.length-this.pos;
if(count<available){
available=count;
}if(available>0){
str.getChars(offset,offset+available,this.buf,this.pos);
this.pos+=available;
}if(this.pos==this.buf.length){
this.out.write(this.buf,0,this.buf.length);
this.pos=0;
if(count>available){
offset+=available;
available=count-available;
if(available>=this.buf.length){
var chars=$_A(count,'\0');
str.getChars(offset,offset+available,chars,0);
this.out.write(chars,0,available);
return;
}str.getChars(offset,offset+available,this.buf,this.pos);
this.pos+=available;
}}}},"~S,~N,~N");
});
