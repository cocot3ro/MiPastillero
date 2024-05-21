Para añadir un MenuManager, usar en onViewCreated(). Ej:

```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.home_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.navigation_notifications -> {
                    true
                }

                R.id.navigation_wallet -> {
                    true
                }

                else -> false
            }
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}
```