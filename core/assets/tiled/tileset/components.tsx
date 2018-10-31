<?xml version="1.0" encoding="UTF-8"?>
<tileset version="1.2" tiledversion="1.2.0" name="components" tilewidth="629" tileheight="630" tilecount="13" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0">
  <image width="431" height="217" source="../resources/botao 1.png"/>
  <objectgroup draworder="index">
   <object id="1" x="80" y="22">
    <polygon points="0,0 -24,82 -56,93 -71,124 -54,149 9,171 127,184 250,172 333,142 342,109 312,86 287,84 265,-2"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="1">
  <image width="457" height="210" source="../resources/botao 2.png"/>
  <objectgroup draworder="index">
   <object id="2" x="0" y="70" width="452" height="128"/>
   <object id="3" x="58" y="2" width="336" height="66"/>
  </objectgroup>
  <animation>
   <frame tileid="1" duration="500"/>
   <frame tileid="13" duration="500"/>
  </animation>
 </tile>
 <tile id="2">
  <image width="236" height="389" source="../resources/emissor 1.png"/>
  <objectgroup draworder="index">
   <object id="1" x="38" y="86">
    <polygon points="0,0 164,-75 164,285 2,213"/>
   </object>
   <object id="2" x="119" y="184">
    <ellipse/>
   </object>
  </objectgroup>
 </tile>
 <tile id="3">
  <image width="462" height="353" source="../resources/emissor 2.png"/>
  <objectgroup draworder="index">
   <object id="1" x="86" y="12">
    <polygon points="0,0 4,112 352,112 350,216 0,214 6,336 -64,258 -64,68"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="4">
  <image width="629" height="545" source="../resources/receptor 1.png"/>
  <objectgroup draworder="index">
   <object id="2" x="18" y="4" width="598" height="522"/>
   <object id="3" x="60" y="112" width="106" height="196"/>
  </objectgroup>
 </tile>
 <tile id="5">
  <image width="316" height="606" source="../resources/receptor 2.png"/>
  <objectgroup draworder="index">
   <object id="1" x="15.1515" y="12.1212" width="272.727" height="569.697"/>
   <object id="3" x="198" y="63" width="67" height="467"/>
  </objectgroup>
 </tile>
 <tile id="7">
  <image width="374" height="356" source="../resources/refletor.png"/>
  <objectgroup draworder="index">
   <object id="1" x="18.1818" y="284.848">
    <polygon points="3.0303,3.0303 106.061,-98.4848 145.455,-269.697 345.455,-9.09091 178.788,54.5455"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="9">
  <image width="100" height="100" source="../resources/bloco.png"/>
  <objectgroup draworder="index">
   <object id="1" x="-0.333333" y="-0.333333" width="100.667" height="100.333"/>
  </objectgroup>
 </tile>
 <tile id="10">
  <image width="135" height="183" source="../resources/luna.png"/>
 </tile>
 <tile id="11">
  <image width="402" height="630" source="../resources/porta 1.png"/>
  <objectgroup draworder="index">
   <object id="1" x="0" y="3.0303" width="406.061" height="621.212"/>
  </objectgroup>
 </tile>
 <tile id="12">
  <image width="367" height="611" source="../resources/porta 2.png"/>
  <objectgroup draworder="index">
   <object id="1" x="3.0303" y="-3.0303" width="366.667" height="609.091"/>
  </objectgroup>
 </tile>
 <tile id="13">
  <image width="457" height="210" source="../resources/botao 2_pressed.png"/>
 </tile>
 <tile id="14">
  <image width="100" height="100" source="../resources/lava.png"/>
  <objectgroup draworder="index">
   <object id="1" x="-1" y="-1" width="101" height="102"/>
  </objectgroup>
 </tile>
</tileset>
