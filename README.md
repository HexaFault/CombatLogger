# CombatLogger  
A fully configurable, modern combat‑tagging system for Paper servers (Java + Bedrock compatible).  
Built by **HexaFault**, using the `dev.rob2.combatlogger` namespace.

---

## ✨ Features

- ⚔️ **Combat tagging** for melee and projectiles  
- 🎯 **Per‑projectile configuration** (arrows, tridents, snowballs, potions, etc.)  
- ⏱️ **ActionBar countdown** (works for Java + Bedrock via Geyser/Floodgate)  
- 🚫 **Command blocking** while in combat  
- 🔥 **Logout punishment options**  
  - Kill player  
  - Drop inventory  
  - Run custom commands  
  - Do nothing  
- 📜 `/combat` command to check remaining time  
- 🧩 Clean, modular code structure  
- ⚙️ Fully configurable via `config.yml`  
- 🧱 Built for Paper 1.20.6+  
- 🧑‍🤝‍🧑 Bedrock support included automatically  

---

## 📦 Installation

1. Download the latest release JAR  
2. Drop it into your server’s `plugins/` folder  
3. Start the server  
4. Edit `config.yml` to your liking  
5. Reload or restart the server  

---

## ⚙️ Configuration Overview

```yaml
combat-time: 15
melee-tagging: true
projectile-tagging: true

projectiles:
  arrow: true
  trident: true
  snowball: false
  egg: false
  fishing_hook: true
  splash_potion: true
  lingering_potion: true

logout-action: "kill"

logout-commands:
  - "broadcast %player% logged out during combat!"
  - "kill %player%"

blocked-commands:
  - spawn
  - home
  - tpa
  - back
  - warp

