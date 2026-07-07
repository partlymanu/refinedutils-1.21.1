# Refined Utils

**Refined Utils** adds utility blocks that extend [Refined Storage 2](https://modrinth.com/mod/refined-storage).

---

## Exposed Interface

The **Exposed Interface** is a block that connects to your Refined Storage 2 network like any other network device, and exposes the entire network's item contents as a standard `IItemHandler`.

This means **any mod that can read a chest or inventory can now read your RS2 network directly** — no extra configuration needed.

<!-- SCREENSHOT: Block placed next to RS2 cables, active texture glowing -->

### Features

- Connects to RS2 networks via cable like any other RS2 device
- Exposes the full network inventory as a standard item handler
- Supports both **insertion** and **extraction**
- **Access mode** toggle — set to insert-only, extract-only, or both
- **Operation log** — right-click to see the last 8 insert/extract operations

---

## Compatibility

The Exposed Interface works with any mod that reads inventories — no special integration required.

some Tested Examples:

### Ars Nouveau — Storage Lectern
Use the Dominion Wand to bind the Exposed Interface to a Storage Lectern. The lectern will display and interact with your entire RS2 network as if it were a single large inventory.

<!-- SCREENSHOT: Ars Nouveau Storage Lectern bound to Exposed Interface, showing network items -->

### Create — Stock Link
Point a Stock Link at the Exposed Interface using a packager to monitor item quantities in your RS2 network and trigger automation based on stock levels.

<!-- SCREENSHOT: Create Stock Link pointed at Exposed Interface -->

### Hoppers and Pipe Mods
These can both pull items out of and push items into your RS2 network through the Exposed Interface.

<!-- SCREENSHOT: Hopper above Exposed Interface, items flowing into network -->
![Demonstration](https://cdn.modrinth.com/data/cached_images/2d9f453272abccc49433bf1c70bc5f87c580122b_0.webp)

---
## Crafting Recipe

<!-- SCREENSHOT: JEI/REI recipe for Exposed Interface -->
![Crafting Recipes ](https://cdn.modrinth.com/data/cached_images/5cbff6f33839abcc6ccf74b6d1615650ab4220c9.png)

---

## License

MIT — open source, do what you want.
