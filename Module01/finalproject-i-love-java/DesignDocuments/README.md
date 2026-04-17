# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project. 
# Design Documents

You may have multiple design documents for this project.

```mermaid
classDiagram
    class FoodItem {
        +String id
        +String name
        +String category
        +int quantity
        +String unit
        +String storageLocation
        +String createdAt
        +String expiryDate
        +Boolean isNew
        +Boolean isUrgent
    }

    class FoodCategory {
        +String id
        +String name
        +String icon
    }

    class FoodDatabase {
        +String foodName
        +int defaultExpiryDays
        +String category
    }

    class Preference {
        +String id
        +String healthGoal
    }

    class Recipe {
        +String id
        +String title
        +String category
        +float matchScore
        +float rating
        +int cookTime
        +int calories
        +String description
        +List~String~ availableIngredients
        +List~String~ missingIngredients
    }

    class GroceryItem {
        +String id
        +String name
        +String category
        +int quantity
        +float price
        +Boolean collected
    }

    class InventoryController {
        +searchSuggestions(text) FoodDatabase[]
        +addItem(foodName) FoodItem
        +filterByCategory(category) FoodItem[]
        +sortByExpiry() FoodItem[]
        +sortByCreatedTime() FoodItem[]
    }

    class PreferenceController {
        +savePreference(healthGoal) Preference
        +getPreference() Preference
    }

    class RecommendationController {
        +getRecommendations(inventory, preference) Recipe[]
        +filterByCategory(category) Recipe[]
        +sortByMatchScore() Recipe[]
        +sortByCookTime() Recipe[]
    }

    class GroceryController {
        +toggleCollected(itemId) GroceryItem
        +updateQuantity(itemId, delta) GroceryItem
        +deleteItem(itemId) void
        +calculateSubtotal() float
        +calculateTax(subtotal) float
        +calculateTotal(subtotal, tax) float
    }

    FoodItem --> FoodCategory : belongs to
    FoodDatabase --> FoodCategory : belongs to
    Recipe --> FoodItem : references ingredients
    GroceryItem --> FoodItem : references
    InventoryController --> FoodItem : manages
    InventoryController --> FoodDatabase : queries suggestions
    PreferenceController --> Preference : manages
    RecommendationController --> Recipe : returns
    RecommendationController --> Preference : reads
    RecommendationController --> FoodItem : reads inventory
    GroceryController --> GroceryItem : manages
```

> **Diagram note:** The diagram above is a **rough sketch** (legacy names like `FoodDatabase`, `storageLocation`). The diagram **below** matches the current codebase and PRD.

```mermaid
classDiagram

    class SmartFridgeApp {
        <<utility>>
        - FOOD_CATALOG_RESOURCE : String
        - RECIPE_RESOURCE : String
        - SmartFridgeApp()
        + main(args : String[]) : void
    }

    class WebApp {
        - inventoryController : InventoryController
        - preferenceController : PreferenceController
        - recommendationController : RecommendationController
        - groceryController : GroceryController
        + WebApp(inventoryController : InventoryController, preferenceController : PreferenceController, recommendationController : RecommendationController, groceryController : GroceryController)
        + start() : void
    }

    class ConsoleApp {
        - inventoryController : InventoryController
        - preferenceController : PreferenceController
        - recommendationController : RecommendationController
        - groceryController : GroceryController
        + ConsoleApp(inventoryController : InventoryController, preferenceController : PreferenceController, recommendationController : RecommendationController, groceryController : GroceryController)
        + start() : void
        + processCommand(input : String) : void
    }

    class HealthGoal {
        <<enumeration>>
        MUSCLE_BUILDING
        FAT_LOSS
        BLOOD_SUGAR_CARE
    }

    class FoodCategory {
        - id : String
        - name : String
        - icon : String
        + FoodCategory(id : String, name : String, icon : String)
        + getId() : String
        + getName() : String
        + getIcon() : String
    }

    class RecipeCategory {
        - id : String
        - name : String
        - icon : String
        + RecipeCategory(id : String, name : String, icon : String)
        + getId() : String
        + getName() : String
        + getIcon() : String
    }

    class FoodCatalogEntry {
        - id : String
        - foodName : String
        - aliases : List~String~
        - defaultExpiryDays : int
        - category : FoodCategory
        + getId() : String
        + getFoodName() : String
        + getAliases() : List~String~
        + getDefaultExpiryDays() : int
        + getCategory() : FoodCategory
    }

    class FoodItem {
        - id : String
        - name : String
        - category : FoodCategory
        - quantity : int
        - unit : String
        - createdAt : String
        - expiryDate : String
        + FoodItem(id : String, name : String, category : FoodCategory, quantity : int, unit : String, createdAt : String, expiryDate : String)
        + getId() : String
        + getName() : String
        + getCategory() : FoodCategory
        + getQuantity() : int
        + getUnit() : String
        + getCreatedAt() : String
        + getExpiryDate() : String
        + isNew() : boolean
        + isUrgent() : boolean
    }

    class Preference {
        - id : String
        - healthGoal : HealthGoal
        + Preference(id : String, healthGoal : HealthGoal)
        + getId() : String
        + getHealthGoal() : HealthGoal
    }

    class Recipe {
        <<see nested types in code>>
        - id : String
        - title : String
        - recipeCategory : RecipeCategory
        - healthTags : List~Recipe_HealthTag~
        - requiredIngredients : List~Recipe_Ingredient~
        - optionalIngredients : List~Recipe_Ingredient~
        - matchScore : double
        - rating : double
        - cookTime : int
        - calories : int
        - description : String
        - availableIngredients : List~String~
        - missingIngredients : List~String~
        - urgentMatchedCount : int
        + loaded(...) Recipe
        + withComputed(...) Recipe
        + getters...
    }

    class Recipe_HealthTag {
        <<enumeration nested in Recipe>>
        HIGH_PROTEIN
        LOW_CALORIE
        BLOOD_SUGAR_FRIENDLY
        BALANCED
    }

    class Recipe_Ingredient {
        <<static nested in Recipe>>
        - name : String
        - quantityText : String
        - optional : boolean
    }

    class GroceryItem {
        - id : String
        - name : String
        - category : FoodCategory
        - quantity : int
        - price : double
        - collected : boolean
        + GroceryItem(id : String, name : String, category : FoodCategory, quantity : int, price : double, collected : boolean)
        + getId() : String
        + getName() : String
        + getCategory() : FoodCategory
        + getQuantity() : int
        + getPrice() : double
        + isCollected() : boolean
    }

    class IFoodCatalog {
        <<interface>>
        + searchSuggestions(prefix : String) : List~FoodCatalogEntry~
        + containsFood(foodName : String) : boolean
        + getDefaultExpiryDays(foodName : String) : int
        + resolveEntry(foodName : String) : Optional~FoodCatalogEntry~
        + canonicalFoodName(raw : String) : String
    }

    class IInventoryService {
        <<interface>>
        + getAllItems() : List~FoodItem~
        + addItem(foodName : String) : FoodItem
        + filterByCategory(categoryName : String) : List~FoodItem~
        + sortByExpiry() : List~FoodItem~
        + sortByCreatedTime() : List~FoodItem~
        + clearInventory() : void
    }

    class IPreferenceService {
        <<interface>>
        + savePreference(goal : HealthGoal) : Preference
        + getPreference() : Preference
        + clearPreference() : void
    }

    class IRecommendationService {
        <<interface>>
        + getRecommendations(inventory : List~FoodItem~, preference : Preference) : List~Recipe~
        + filterByRecipeCategory(categoryName : String) : List~Recipe~
        + sortByMatchScore() : List~Recipe~
        + sortByCookTime() : List~Recipe~
        + clearRecommendations() : void
    }

    class IGroceryService {
        <<interface>>
        + getItems() : List~GroceryItem~
        + toggleCollected(itemId : String) : GroceryItem
        + updateQuantity(itemId : String, delta : int) : GroceryItem
        + deleteItem(itemId : String) : void
        + calculateSubtotal() : double
        + calculateTax(subtotal : double) : double
        + calculateTotal(subtotal : double, tax : double) : double
        + checkout() : void
        + clearGrocery() : void
    }

    class FoodCatalog {
        - entries : List~FoodCatalogEntry~
        + FoodCatalog(entries : List~FoodCatalogEntry~)
        + searchSuggestions(prefix : String) : List~FoodCatalogEntry~
        + containsFood(foodName : String) : boolean
        + getDefaultExpiryDays(foodName : String) : int
        + resolveEntry(foodName : String) : Optional~FoodCatalogEntry~
        + canonicalFoodName(raw : String) : String
    }

    class InventoryService {
        - items : List~FoodItem~
        - foodCatalog : IFoodCatalog
        + InventoryService(foodCatalog : IFoodCatalog)
        + getAllItems() : List~FoodItem~
        + addItem(foodName : String) : FoodItem
        + filterByCategory(categoryName : String) : List~FoodItem~
        + sortByExpiry() : List~FoodItem~
        + sortByCreatedTime() : List~FoodItem~
        + clearInventory() : void
    }

    class PreferenceService {
        - currentPreference : Preference
        + PreferenceService()
        + savePreference(goal : HealthGoal) : Preference
        + getPreference() : Preference
        + clearPreference() : void
    }

    class RecommendationService {
        - recipeTemplates : List~Recipe~
        - foodCatalog : IFoodCatalog
        - currentRecommendations : List~Recipe~
        + RecommendationService(recipes : List~Recipe~, foodCatalog : IFoodCatalog)
        + getRecommendations(inventory : List~FoodItem~, preference : Preference) : List~Recipe~
        + filterByRecipeCategory(categoryName : String) : List~Recipe~
        + sortByMatchScore() : List~Recipe~
        + sortByCookTime() : List~Recipe~
        + clearRecommendations() : void
    }

    class GroceryService {
        - TAX_RATE : double
        - groceryItems : List~GroceryItem~
        + GroceryService(items : List~GroceryItem~)
        + getItems() : List~GroceryItem~
        + toggleCollected(itemId : String) : GroceryItem
        + updateQuantity(itemId : String, delta : int) : GroceryItem
        + deleteItem(itemId : String) : void
        + calculateSubtotal() : double
        + calculateTax(subtotal : double) : double
        + calculateTotal(subtotal : double, tax : double) : double
        + checkout() : void
        + clearGrocery() : void
    }

    class JsonFoodCatalogLoader {
        <<utility>>
        - JsonFoodCatalogLoader()
        + loadFromFile(path : String) : List~FoodCatalogEntry~
        + loadFromFileSafe(path : String) : List~FoodCatalogEntry~
    }

    class JsonRecipeLoader {
        <<utility>>
        - JsonRecipeLoader()
        + loadFromFile(path : String) : List~Recipe~
        + loadFromFileSafe(path : String) : List~Recipe~
    }

    class SessionReset {
        <<utility>>
        + clearAll(preferenceService, inventoryService, recommendationService, groceryService) : void
    }

    class FoodCatalogJsonFile {
        <<file>>
        - path : String
    }

    class RecipeJsonFile {
        <<file>>
        - path : String
    }

    class InventoryController {
        - inventoryService : IInventoryService
        + InventoryController(inventoryService : IInventoryService)
        + getVisibleItems() : List~FoodItem~
        + addItem(foodName : String) : FoodItem
        + filterByCategory(categoryName : String) : List~FoodItem~
        + sortByExpiry() : List~FoodItem~
        + sortByCreatedTime() : List~FoodItem~
    }

    class PreferenceController {
        - preferenceService : IPreferenceService
        + PreferenceController(preferenceService : IPreferenceService)
        + savePreference(goal : HealthGoal) : Preference
        + getPreference() : Preference
    }

    class RecommendationController {
        - recommendationService : IRecommendationService
        + RecommendationController(recommendationService : IRecommendationService)
        + getRecommendations(inventory : List~FoodItem~, preference : Preference) : List~Recipe~
        + filterByRecipeCategory(categoryName : String) : List~Recipe~
        + sortByMatchScore() : List~Recipe~
        + sortByCookTime() : List~Recipe~
    }

    class GroceryController {
        - groceryService : IGroceryService
        - onCheckoutLoopEnd : Runnable
        + GroceryController(groceryService : IGroceryService)
        + GroceryController(groceryService : IGroceryService, onCheckoutLoopEnd : Runnable)
        + toggleCollected(itemId : String) : GroceryItem
        + updateQuantity(itemId : String, delta : int) : GroceryItem
        + deleteItem(itemId : String) : void
        + calculateSubtotal() : double
        + calculateTax(subtotal : double) : double
        + calculateTotal(subtotal : double, tax : double) : double
        + checkout() : void
    }

    class WelcomePage {
        + render() : void
    }

    class PreferencePage {
        - preferenceController : PreferenceController
        + render() : void
        + submitGoal(goal : HealthGoal) : void
    }

    class InventoryPage {
        - inventoryController : InventoryController
        + render() : void
    }

    class AddItemModal {
        - inventoryController : InventoryController
        + render() : void
        + submitFoodName(foodName : String) : void
    }

    class RecommendationPage {
        - recommendationController : RecommendationController
        + render() : void
    }

    class RecipeDetailPage {
        - recommendationController : RecommendationController
        + render() : void
    }

    class GroceryPage {
        - groceryController : GroceryController
        + render() : void
        + submitCheckout() : void
    }

    FoodItem --> FoodCategory : belongs to
    Preference --> HealthGoal : selected
    FoodCatalogEntry --> FoodCategory : classified as
    Recipe --> RecipeCategory : belongs to
    Recipe --> Recipe_HealthTag : uses
    Recipe --> Recipe_Ingredient : contains
    Recipe --> FoodItem : matches against
    GroceryItem --> FoodCategory : classified as

    FoodCatalog ..|> IFoodCatalog
    InventoryService ..|> IInventoryService
    PreferenceService ..|> IPreferenceService
    RecommendationService ..|> IRecommendationService
    GroceryService ..|> IGroceryService

    JsonFoodCatalogLoader --> FoodCatalogJsonFile : reads
    JsonFoodCatalogLoader --> FoodCatalogEntry : loads
    JsonRecipeLoader --> RecipeJsonFile : reads
    JsonRecipeLoader --> Recipe : loads

    FoodCatalog --> FoodCatalogEntry : stores
    InventoryService --> IFoodCatalog : uses
    RecommendationService --> Preference : reads
    RecommendationService --> FoodItem : reads
    RecommendationService --> Recipe : uses
    RecommendationService --> IFoodCatalog : normalizes names
    GroceryService --> GroceryItem : manages

    InventoryController --> IInventoryService : uses
    PreferenceController --> IPreferenceService : uses
    RecommendationController --> IRecommendationService : uses
    GroceryController --> IGroceryService : uses

    WebApp --> WelcomePage : shows
    WebApp --> PreferencePage : shows
    WebApp --> InventoryPage : shows
    WebApp --> RecommendationPage : shows
    WebApp --> RecipeDetailPage : shows
    WebApp --> GroceryPage : shows

    ConsoleApp --> InventoryController : uses
    ConsoleApp --> PreferenceController : uses
    ConsoleApp --> RecommendationController : uses
    ConsoleApp --> GroceryController : uses

    SmartFridgeApp --> JsonFoodCatalogLoader : initializes
    SmartFridgeApp --> JsonRecipeLoader : initializes
    SmartFridgeApp --> FoodCatalog : builds
    SmartFridgeApp --> InventoryService : builds
    SmartFridgeApp --> PreferenceService : builds
    SmartFridgeApp --> RecommendationService : builds
    SmartFridgeApp --> GroceryService : builds
    SmartFridgeApp --> InventoryController : builds
    SmartFridgeApp --> PreferenceController : builds
    SmartFridgeApp --> RecommendationController : builds
    SmartFridgeApp --> GroceryController : builds
    SmartFridgeApp --> SessionReset : uses on checkout
    SmartFridgeApp --> WebApp : starts
    SmartFridgeApp --> ConsoleApp : optionally starts

    WelcomePage --> PreferencePage : routes to
    PreferencePage --> PreferenceController : uses
    InventoryPage --> InventoryController : uses
    AddItemModal --> InventoryController : uses
    RecommendationPage --> RecommendationController : uses
    RecipeDetailPage --> RecommendationController : uses
    GroceryPage --> GroceryController : uses

    note for ConsoleApp "Optional input shell. Added to match the teacher-style console interaction layer."
    note for RecipeCategory "Separate category type for recipes. FoodCategory is for ingredients; RecipeCategory is for dishes."
    note for Recipe "PRD fields: enum HealthTag and static class Ingredient are nested inside Recipe.java (not separate top-level files)."
    note for SessionReset "PRD: after checkout, clears preference, inventory, recommendations, grocery (no persistence)."
```



```mermaid
classDiagram

    class TestFoodCatalog {
        <<Test>>
        - foodCatalog : FoodCatalog
        + setUp() : void
        + testSearchSuggestionsReturnsMatchingEntries() : void
        + testContainsFoodReturnsTrueWhenFoodExists() : void
        + testGetDefaultExpiryDaysReturnsConfiguredValue() : void
    }

    class TestInventoryService {
        <<Test>>
        - inventoryService : InventoryService
        - foodCatalog : IFoodCatalog
        + setUp() : void
        + testGetAllItemsReturnsCurrentInventory() : void
        + testAddItemCreatesFoodItemFromCatalog() : void
        + testFilterByCategoryReturnsMatchingItems() : void
        + testSortByExpiryOrdersUrgentFirst() : void
        + testSortByCreatedTimeOrdersNewestFirst() : void
        + testClearInventoryRemovesAllItems() : void
    }

    class TestPreferenceService {
        <<Test>>
        - preferenceService : PreferenceService
        + setUp() : void
        + testSavePreferenceStoresGoal() : void
        + testGetPreferenceReturnsSavedGoal() : void
        + testClearPreferenceRemovesCurrentPreference() : void
    }

    class TestRecommendationService {
        <<Test>>
        - recommendationService : RecommendationService
        - recipes : List~Recipe~
        + setUp() : void
        + testGetRecommendationsUsesInventoryAndPreference() : void
        + testFilterByRecipeCategoryReturnsMatchingRecipes() : void
        + testSortByMatchScoreOrdersDescending() : void
        + testSortByCookTimeOrdersAscending() : void
        + testClearRecommendationsRemovesCurrentResults() : void
    }

    class TestGroceryService {
        <<Test>>
        - groceryService : GroceryService
        + setUp() : void
        + testGetItemsReturnsCurrentGroceryItems() : void
        + testToggleCollectedUpdatesState() : void
        + testUpdateQuantityChangesAmount() : void
        + testDeleteItemRemovesItem() : void
        + testCalculateSubtotalUsesCollectedItems() : void
        + testCalculateTaxUsesEightPercent() : void
        + testCalculateTotalAddsSubtotalAndTax() : void
        + testCheckoutClearsGroceryItems() : void
        + testClearGroceryRemovesAllItems() : void
    }

    class TestInventoryController {
        <<Test>>
        - inventoryController : InventoryController
        - inventoryService : IInventoryService
        + setUp() : void
        + testGetVisibleItemsDelegatesToInventoryService() : void
        + testAddItemDelegatesToInventoryService() : void
        + testFilterByCategoryDelegatesToInventoryService() : void
        + testSortByExpiryDelegatesToInventoryService() : void
        + testSortByCreatedTimeDelegatesToInventoryService() : void
    }

    class TestPreferenceController {
        <<Test>>
        - preferenceController : PreferenceController
        - preferenceService : IPreferenceService
        + setUp() : void
        + testSavePreferenceDelegatesToPreferenceService() : void
        + testGetPreferenceDelegatesToPreferenceService() : void
    }

    class TestRecommendationController {
        <<Test>>
        - recommendationController : RecommendationController
        - recommendationService : IRecommendationService
        + setUp() : void
        + testGetRecommendationsDelegatesToRecommendationService() : void
        + testFilterByRecipeCategoryDelegatesToRecommendationService() : void
        + testSortByMatchScoreDelegatesToRecommendationService() : void
        + testSortByCookTimeDelegatesToRecommendationService() : void
    }

    class TestGroceryController {
        <<Test>>
        - groceryController : GroceryController
        - groceryService : IGroceryService
        + setUp() : void
        + testToggleCollectedDelegatesToGroceryService() : void
        + testUpdateQuantityDelegatesToGroceryService() : void
        + testDeleteItemDelegatesToGroceryService() : void
        + testCalculateSubtotalDelegatesToGroceryService() : void
        + testCalculateTaxDelegatesToGroceryService() : void
        + testCalculateTotalDelegatesToGroceryService() : void
        + testCheckoutDelegatesToGroceryService() : void
    }

    class TestConsoleApp {
        <<Test>>
        - consoleApp : ConsoleApp
        - inventoryController : InventoryController
        - preferenceController : PreferenceController
        - recommendationController : RecommendationController
        - groceryController : GroceryController
        + setUp() : void
        + testStartInitializesConsoleFlow() : void
        + testProcessCommandRoutesInventoryCommand() : void
        + testProcessCommandRoutesPreferenceCommand() : void
        + testProcessCommandRoutesRecommendationCommand() : void
        + testProcessCommandRoutesGroceryCommand() : void
        + testProcessCommandHandlesInvalidInput() : void
    }

    class FoodCatalog
    class InventoryService
    class PreferenceService
    class RecommendationService
    class GroceryService
    class InventoryController
    class PreferenceController
    class RecommendationController
    class GroceryController
    class ConsoleApp
    class IFoodCatalog
    class IInventoryService
    class IPreferenceService
    class IRecommendationService
    class IGroceryService
    class RecipeCategory

    TestFoodCatalog --> FoodCatalog : tests
    TestInventoryService --> InventoryService : tests
    TestInventoryService --> IFoodCatalog : uses test double
    TestPreferenceService --> PreferenceService : tests
    TestRecommendationService --> RecommendationService : tests
    TestGroceryService --> GroceryService : tests

    TestInventoryController --> InventoryController : tests
    TestInventoryController --> IInventoryService : mocks
    TestPreferenceController --> PreferenceController : tests
    TestPreferenceController --> IPreferenceService : mocks
    TestRecommendationController --> RecommendationController : tests
    TestRecommendationController --> IRecommendationService : mocks
    TestGroceryController --> GroceryController : tests
    TestGroceryController --> IGroceryService : mocks

    TestConsoleApp --> ConsoleApp : tests
    TestConsoleApp --> InventoryController : uses
    TestConsoleApp --> PreferenceController : uses
    TestConsoleApp --> RecommendationController : uses
    TestConsoleApp --> GroceryController : uses

    TestRecommendationService --> RecipeCategory : verifies filtering on
    TestRecommendationController --> RecipeCategory : verifies filtering on
```