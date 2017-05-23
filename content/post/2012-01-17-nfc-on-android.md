---
date: 2012-01-17T00:00:00Z
published: true
status: publish
tags: ["nfc", "android"]
title: NFC on Android
type: post
url: /2012/01/17/nfc-on-android/
---

What's NFC
----------
* NFC is a set of short-range wireless technologies, typically requiring a distance of 4 cm or less. 
* NFC operates at 13.56 MHz on ISO/IEC 18000-3 air interface and at rates ranging from 106 kbit/s to 424 kbit/s.
* NFC always involves an initiator and a target; the initiator actively generates an RF field that can power a passive target.
* NFC peer-to-peer communication is possible, provided both devices are powered.
* There is no link level encryption built into NFC


On Android
----------
* NFC allows you to share small payloads of data between an NFC tag and an Android-powered device, or between two Android-powered devices.
* Android framework APIs are based around a NFC Forum standard called **NDEF** (NFC Data Exchange Format).
* On Android NFC is off when the screen is off (no butt sniffing)
* No API support for card emulation

Supported Tag Technology
------------------------

It is **mandatory** for all Android NFC devices to support the following tags:
<table>
<tr><th>Class</th><th>Description</th></tr>
<tr><td>NfcA</td><td>Provides access to NFC-A (ISO 14443-3A) properties and I/O operations.</td></tr>
<tr><td>NfcB</td><td>Provides access to NFC-B (ISO 14443-3B) properties and I/O operations.</td></tr>
<tr><td>NfcF</td><td>Provides access to NFC-F (JIS 6319-4) properties and I/O operations.</td></tr>
<tr><td>NfcV</td><td>Provides access to NFC-V (ISO 15693) properties and I/O operations.</td></tr>
<tr><td>IsoDep</td><td>Provides access to ISO-DEP (ISO 14443-4) properties and I/O operations.</td></tr>
<tr><td>Ndef</td><td>Provides access to NDEF data and operations on NFC tags that have been formatted as NDEF. Support NFC Forum Type 1, Type 2, Type 3 or Type 4 compliant tags</td></tr>
</table>

The following tag technologies are optionally supported:
<table>
<tr><th>Class</th><th>Description</th></tr>
<tr><td>MifareClassicProvides</td><td>access to MIFARE Classic properties and I/O operations, if this Android device supports MIFARE.</td></tr>
<tr><td>MifareUltralight</td><td>Provides access to MIFARE Ultralight properties and I/O operations, if this Android device supports MIFARE.</td></tr>
</table>

Relationships between buzz words:

![](/images/android-nfc-beyond-ndef.png "Relationships between buzz words")
