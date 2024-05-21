cleari()
// ES is edge size of 'world' 
val ES=128; val AS=ES*ES
// Initialize vector to all dead cells
var v = (0 until AS).foldLeft(Vector[Int]())((x,y)=> x :+ 0 )

v=init(v,fpent) // Choose initial pattern from those below

// animation is about 30 frames per second or 32 milliseconds per frame
var t=0
animate {
   if(t%1 == 0){
     erasePictures()
     disp(v)
     v = (0 until AS).foldLeft(Vector[Int]())((x,y)=>x :+ ns(v,y)) 
   }
   if (t == 1103) stopAnimation
   t+=1
}
// New generation
def ns(v:Vector[Int],ix:Int)={
 val rule=Vector(0,0,0,1,1,0,0,0,0,0) // life rules
 val x=ix/ES ; val y=ix%ES
 val t = (0 until 3).foldLeft(0)((st,i)=>{
     st + (0 until 3).foldLeft(0)((s,j)=>{
            val xt=x+i-1 ; val yt=y+j-1
            s+(if((xt<0)||(xt>=ES)||(yt<0)||(yt>=ES)) 0 else v(xt*ES+yt))   
            })
     })
   if(v(ix)==1) rule(t) else {if (t==3) 1 else 0}
}
// display cells
def disp(v:Vector[Int])= for(i<- 0 until AS)
    if(v(i)==1) draw( fillColor(blue) * trans((i/ES)*10-ES*5,(i%ES)*10-ES*5) -> Picture.circle(5) )
// set up starting pattern   
def init(v:Vector[Int],p:List[(Int,Int)]) = p.foldLeft(v)((x,y)=> x.updated( (y._1 + ES/2)* ES+y._2 + ES/2,1))

// Some well known starting patterns  
def fpent=List((0,1),(1,0),(1,1),(1,2),(2,2))
def diehard=List((0,1),(1,0),(1,1),(5,0),(6,0),(7,0),(6,2))
def acorn=List((0,0),(1,0),(1,2),(3,1),(4,0),(5,0),(6,0))
def glider=List((-18,3),(-18,4),(-17,3),(-17,4),(-8,2),(-8,3),(-8,4),(-7,1),(-7,5),
    (-6,0),(-6,6),(-5,0),(-5,6),(-4,3),(-3,1),(-3,5),(-2,2),(-2,3),(-2,4),
    (-1,3),(2,4),(2,5),(2,6),(3,4),(3,5),(3,6),(4,3),(4,7),
    (6,2),(6,3),(6,7),(6,8),(16,5),(16,6),(17,5),(17,6))
def block1=List((0,0),(2,0),(2,1),(4,2),(4,3),(4,4),(6,3),(6,4),(6,5),(7,4)) 
def block2=List((0,0),(0,3),(0,4),(1,1),(1,4),(2,0),(2,1),(2,4),(3,2),(4,0),
            (4,1),(4,2),(4,4))
def tiny=List((-18,0),(-17,0),(-16,0),(-15,0),(-14,0),(-13,0),(-12,0),(-11,0),(-9,0),(-8,0),
        (-7,0),(-6,0),(-5,0),(-1,0),(0,0),(1,0),(8,0),(9,0),(10,0),
    (11,0),(12,0),(13,0),(14,0),(16,0),(17,0),(18,0),(19,0),(20,0)) 
