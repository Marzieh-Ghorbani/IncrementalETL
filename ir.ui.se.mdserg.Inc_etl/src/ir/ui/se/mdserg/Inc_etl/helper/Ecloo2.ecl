pre{
 
   var sys = Native("java.lang.System");
  	var startTime=sys.currentTimeMillis();

}
  
  
rule NamedElements
     match l : Left!NamedElement
     with r : Right!NamedElement{
     compare : l.name= r.name
     
}
     
 
rule PackageableElements
     match l : Left! PackageableElement
     with r : Right! PackageableElement
     {
         compare : 
         l.package.matches(r.package)
         and l.name = r.name
 }
    

rule packages
   match l : Left!Package
    with r : Right!Package{
     compare : l.name = r.name
}
    

rule class
   match l : Left!Class
    with r : Right!Class
    {
     compare : l.name = r.name
}

 
rule Features
    match l : Left!Feature
    with r : Right!Feature
  	{
       compare : 
       l.owner.matches(r.owner)
       and l.name = r.name
}
       
       
rule Operations
    match l : Left!Operation
    with r : Right!Operation
   {
     compare :	l.name = r.name 
}

  
rule Attributs
      match l :Left!Attribute
      with r : Right!Attribute
      {
      compare : l.name = r.name
}
          
          
rule References
  	match l :Left!Reference
  	with r : Right!Reference
  	{
  	compare : l.name = r.name
}
        
        
post{                   
    var stopTime=sys.currentTimeMillis();
	var elapsedTime= stopTime-startTime;

}
             
             
                  
  