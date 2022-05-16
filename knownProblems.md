## 已知的问题

---

正在使用灭火器时，使用滚轮切换物品栏会导致tag`using`不能正确地被复位为false。

滚轮切换物品栏不会触发`releaseUsing`或`finishUsingItem`方法。尝试使用`shouldCauseReequipAnimation`方法但效果不好。

需要tag`using`是因为之前使用ItemPropertyOverride时`pEntity.getUseItem() == pStack`会导致物品闪烁（why?）。

---

持续右键使用灭火器时可能出现不稳定。不影响功能，只影响外观。退出至主菜单重进即可。

---