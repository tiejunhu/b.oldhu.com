+++
draft = false
toc = false
date = "2022-09-23T17:50:15+08:00"
title = "Office XML(OOXML, docx)中的长度单位"
+++
## 基本前提
* A4 尺寸：210mm x 297mm = 8.27in x 11.69in
* Point Per Inch: 72
* Pixel Per Inch: 96
* 1 inch = 2.54 cm

## 主单位 dxa / twips
dxa的大小 是 point 的 1/20

所以可以计算出：

* A4的Point尺寸：8.27 * 72 x 11.69 * 72 = 595.44 x 841.68
* A4的dxa尺寸：595.44 * 20 x 841.68 * 20 = 11906 x 16838

## emu (English Metric Unit)
* 1 Inch = 914400 EMU
* 1 Inch = 72 pt = 1440 dxa

### 所以
* DXA to EMU: 1 dxa = 914400 / 1440 = 635 emu
* Pixel to EMU: pixel \* (914400 / 96) = pixel \* 9525
* Pixel to DXA: pixel \* (1440 / 96) = pixel \* 15
* Pixel to PT: pixel \* 72 / 96 = pixel \* 0.75
* PT to Pixel: pt \* 96 / 72 = pt \* 1.3333

