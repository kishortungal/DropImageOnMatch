# DropImageOnMatch
Drag and Drop Image on Match

Dependency Lib: Salenium, OpenCV

Problem Statement:  You have a source image say triangle in a website and you want to drag and drop that image on another image (Destination Image) which has multiple image inside them including triangle but you want to drop exactly like it covers image you are dragging covers triangle inside destination Image.

Solution: 
Use Salenium to go to the webpage then 
Download both images source and destination
Find source image match in destination image use OpenCV and get its co-ordinates (x, y)
Use selenium to Drag and drop it in particular location.

GITHub Code:
https://github.com/kishortungal/DropImageOnMatch
