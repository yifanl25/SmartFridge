#!/usr/bin/env python3
"""Convert origin/Yifan Data/*.json into PRD-shaped food_catalog.json and recipes.json."""
import json
import re
import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent


def git_show(spec: str) -> str:
    r = subprocess.run(
        ["git", "show", spec],
        cwd=str(ROOT),
        capture_output=True,
        text=True,
        check=True,
    )
    return r.stdout


def slug(s: str) -> str:
    s = re.sub(r"[^a-z0-9]+", "_", s.lower().strip())
    return s.strip("_") or "cat"


def map_health_tags(raw_tags: list) -> list:
    out = []
    for t in raw_tags or []:
        tl = str(t).lower().replace(" ", "-")
        if "high-protein" in tl or "high protein" in tl or tl == "protein":
            out.append("HIGH_PROTEIN")
        elif "low-cal" in tl or "low calorie" in tl:
            out.append("LOW_CALORIE")
        elif "blood" in tl or "sugar" in tl or "diabetes" in tl:
            out.append("BLOOD_SUGAR_FRIENDLY")
        elif "balanced" in tl:
            out.append("BALANCED")
    if not out:
        out.append("BALANCED")
    # dedupe preserve order
    seen = set()
    deduped = []
    for x in out:
        if x not in seen:
            seen.add(x)
            deduped.append(x)
    return deduped


def main() -> None:
    inv = json.loads(git_show("origin/Yifan:Data/food_inventory.json"))
    recipes_src = json.loads(git_show("origin/Yifan:Data/recipes.json"))

    entries = []
    for cat in inv.get("inventory", {}).get("categories", []):
        cid = cat.get("id", slug(cat.get("name", "cat")))
        cname = cat.get("name", "Other")
        cicon = slug(cname)
        for it in cat.get("items", []):
            name = it.get("name", "").strip()
            if not name:
                continue
            days = int(it.get("duration_days", 7))
            alias_bits = [a for a in (it.get("tags") or []) if isinstance(a, str)]
            aliases = list({a for a in alias_bits if a and a.lower() != name.lower()})
            entries.append(
                {
                    "id": it.get("id") or f"item_{slug(name)}",
                    "foodName": name,
                    "aliases": aliases[:12],
                    "defaultExpiryDays": days,
                    "category": {"id": cid, "name": cname, "icon": cicon},
                }
            )

    out_catalog = {"entries": entries}
    (ROOT / "src/main/resources/food_catalog.json").write_text(
        json.dumps(out_catalog, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )

    recipes_out = []
    for r in recipes_src.get("recipes", []):
        rid = r.get("id", "")
        title = r.get("name", "Untitled")
        cat_name = r.get("category", "General")
        rc_id = f"rc_{slug(cat_name)}"
        rc = {"id": rc_id, "name": cat_name, "icon": slug(cat_name)}
        req = []
        for ing in r.get("ingredients", []):
            nm = ing.get("name", "").strip()
            if not nm:
                continue
            amt = ing.get("amount")
            unit = ing.get("unit", "")
            qty = f"{amt} {unit}".strip()
            req.append({"name": nm, "quantityText": qty, "optional": False})
        desc = "\n".join(r.get("instructions", [])[:6]) if r.get("instructions") else ""
        recipes_out.append(
            {
                "id": rid,
                "title": title,
                "recipeCategory": rc,
                "healthTags": map_health_tags(r.get("tags", [])),
                "requiredIngredients": req,
                "optionalIngredients": [],
                "rating": float(r.get("rating", 4.0)),
                "cookTime": int(r.get("cook_time_min", 20)),
                "calories": int(r.get("calories", 300)),
                "description": desc[:2000],
            }
        )

    out_recipes = {"recipes": recipes_out}
    (ROOT / "src/main/resources/recipes.json").write_text(
        json.dumps(out_recipes, indent=2, ensure_ascii=False) + "\n",
        encoding="utf-8",
    )
    print(f"Wrote food_catalog.json ({len(entries)} entries) and recipes.json ({len(recipes_out)} recipes)")


if __name__ == "__main__":
    main()
