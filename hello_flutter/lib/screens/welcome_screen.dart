import 'package:flutter/material.dart';

// Exact Figma color tokens
const _bg = Color(0xFFF5EDE0);
const _nav = Color(0xFFFFFCF7);
const _dark = Color(0xFF3D1A0E);
const _brown = Color(0xFF7B3B2A);
const _gold = Color(0xFFA0704A);
const _body = Color(0xFF9A7A60);
const _list = Color(0xFF7A5A40);
const _muted = Color(0xFFB09070);
const _card = Color(0xFFFFFCF7);
const _iconBg = Color(0xFFF0E6D6);
const _warn = Color(0xFFE07B39);

class WelcomePage extends StatelessWidget {
  const WelcomePage({super.key});

  @override
  Widget build(BuildContext context) => Scaffold(
    backgroundColor: _bg,
    body: SingleChildScrollView(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: const [
          _NavBar(),
          _HeroSection(),
          _FeaturesSection(),
          _StatsBar(),
        ],
      ),
    ),
  );
}

class _NavBar extends StatelessWidget {
  const _NavBar();

  @override
  Widget build(BuildContext context) {
    final wide = MediaQuery.of(context).size.width > 980;
    return Container(
      color: _nav,
      padding: EdgeInsets.symmetric(horizontal: wide ? 60 : 20, vertical: 17),
      child: Row(
        children: [
          Row(
            children: [
              Container(
                width: 28,
                height: 28,
                decoration: BoxDecoration(
                  color: _brown,
                  borderRadius: BorderRadius.circular(7),
                ),
                child: const Icon(Icons.kitchen, color: _nav, size: 14),
              ),
              const SizedBox(width: 8),
              const Text(
                'SmartFridge',
                style: TextStyle(
                  fontSize: 17,
                  fontWeight: FontWeight.w800,
                  color: _dark,
                ),
              ),
            ],
          ),
          const Spacer(),
        if (wide) ...[
            _NavLink('HOME', active: true),
            _NavLink('FRIDGE'),
            _NavLink('RECIPES'),
            _NavLink('PRICING'),
            const SizedBox(width: 28),
            const Text(
              'Sign In',
              style: TextStyle(
                fontSize: 13,
                fontWeight: FontWeight.w600,
                color: _dark,
              ),
            ),
            const SizedBox(width: 14),
          ],
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 9),
            decoration: BoxDecoration(
              color: _brown,
              borderRadius: BorderRadius.circular(24),
            ),
            child: const Text(
              'LOG IN',
              style: TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w700,
                color: _nav,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _NavLink extends StatelessWidget {
  final String label;
  final bool active;
  const _NavLink(this.label, {this.active = false});

  @override
  Widget build(BuildContext context) => Padding(
    padding: const EdgeInsets.symmetric(horizontal: 16),
    child: Text(
      label,
      style: TextStyle(
        fontSize: 12,
        fontWeight: FontWeight.w600,
        color: active ? _brown : _muted,
        decoration: active ? TextDecoration.underline : null,
        decorationColor: _brown,
        decorationThickness: 2,
      ),
    ),
  );
}

class _HeroSection extends StatelessWidget {
  const _HeroSection();

  @override
  Widget build(BuildContext context) {
    final wide = MediaQuery.of(context).size.width > 860;
    if (wide) {
      return Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Expanded(
            flex: 50,
            child: Padding(
              padding: const EdgeInsets.fromLTRB(60, 52, 20, 64),
              child: _HeroLeft(),
            ),
          ),
          const Expanded(flex: 50, child: _HeroRight()),
        ],
      );
    }
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.fromLTRB(20, 40, 20, 0),
          child: _HeroLeft(),
        ),
        const SizedBox(height: 40),
        const Padding(
          padding: EdgeInsets.symmetric(horizontal: 20),
          child: _HeroRight(),
        ),
        const SizedBox(height: 40),
      ],
    );
  }
}

class _HeroLeft extends StatelessWidget {
  static const _checks = [
    'Track ingredients & expiry dates automatically',
    'Get personalised recipe suggestions daily',
    "Build grocery lists from what's missing",
  ];

  @override
  Widget build(BuildContext context) => Column(
    crossAxisAlignment: CrossAxisAlignment.start,
    children: [
      const Text(
        'YOUR FRIDGE, SMARTER',
        style: TextStyle(
          fontSize: 11,
          fontWeight: FontWeight.w700,
          color: _gold,
          letterSpacing: 1.3,
        ),
      ),
      const SizedBox(height: 14),
      const Text(
        'Never Let Food',
        style: TextStyle(
          fontSize: 52,
          fontWeight: FontWeight.w900,
          color: _dark,
          height: 1.05,
          letterSpacing: -1,
        ),
      ),
      const Text(
        'Go to Waste',
        style: TextStyle(
          fontSize: 52,
          fontWeight: FontWeight.w900,
          color: _gold,
          height: 1.05,
          letterSpacing: -1,
        ),
      ),
      const SizedBox(height: 18),
      const Text(
        'SmartFridge tracks every item in your fridge, alerts you before things expire, and suggests recipes from what you already have — saving money and reducing waste.',
        style: TextStyle(fontSize: 14, color: _body, height: 1.65),
      ),
      const SizedBox(height: 18),
      for (final item in _checks) ...[
        Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 2),
              child: Container(
                width: 16,
                height: 16,
                decoration: const BoxDecoration(
                  color: _dark,
                  shape: BoxShape.circle,
                ),
                child: const Icon(Icons.check, color: _nav, size: 10),
              ),
            ),
            const SizedBox(width: 10),
            Expanded(
              child: Text(
                item,
                style: const TextStyle(fontSize: 13, color: _list, height: 1.5),
              ),
            ),
          ],
        ),
        const SizedBox(height: 8),
      ],
      const SizedBox(height: 22),
      Row(
        children: [
          ElevatedButton(
            onPressed: () => Navigator.pushNamed(context, '/preference'),
            style: ElevatedButton.styleFrom(
              backgroundColor: _dark,
              foregroundColor: _nav,
              padding: const EdgeInsets.symmetric(horizontal: 28, vertical: 14),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(30),
              ),
              elevation: 0,
              textStyle: const TextStyle(fontSize: 12, fontWeight: FontWeight.w700),
            ),
            child: const Text('GET STARTED FREE'),
          ),
          const SizedBox(width: 12),
          OutlinedButton(
            onPressed: null,
            style: OutlinedButton.styleFrom(
              side: const BorderSide(color: _brown, width: 2),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(30),
              ),
              padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 14),
              disabledForegroundColor: _dark,
            ),
            child: const Text(
              'MORE DETAIL',
              style: TextStyle(fontSize: 12, fontWeight: FontWeight.w700, color: _dark),
            ),
          ),
        ],
      ),
      const SizedBox(height: 24),
      Row(
        children: [
          SizedBox(
            width: 64,
            height: 28,
            child: Stack(
              children: [
                for (var i = 0; i < 3; i++)
                  Positioned(
                    left: i * 17.0,
                    child: Container(
                      width: 28,
                      height: 28,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: const [
                          Color(0xFFFFD4B2),
                          Color(0xFFB8D8B8),
                          Color(0xFFB8C8E8),
                        ][i],
                        border: Border.all(color: _nav, width: 1.5),
                      ),
                      child: Icon(
                        Icons.person,
                        size: 13,
                        color: _dark.withValues(alpha: 0.4),
                      ),
                    ),
                  ),
              ],
            ),
          ),
          const SizedBox(width: 10),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  for (var i = 0; i < 4; i++) const Icon(Icons.star, size: 12, color: _gold),
                  const Icon(Icons.star_half, size: 12, color: _gold),
                ],
              ),
              const SizedBox(height: 2),
              const Text(
                'Loved by 12,000+ households',
                style: TextStyle(fontSize: 11, color: _muted),
              ),
            ],
          ),
        ],
      ),
    ],
  );
}

class _HeroRight extends StatelessWidget {
  const _HeroRight();

  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (ctx, constraints) {
        final w = constraints.maxWidth;
        final h = w * (658.0 / 780.0);
        final circleSize = w * (590.0 / 780.0);

        return SizedBox(
          width: w,
          height: h,
          child: Stack(
            clipBehavior: Clip.hardEdge,
            children: [
              Positioned(
                top: 0,
                left: -w * 0.072,
                child: Container(
                  width: w * 0.955,
                  height: h * 1.026,
                  decoration: BoxDecoration(
                    color: const Color(0xFFC4956A),
                    borderRadius: BorderRadius.circular(w),
                  ),
                ),
              ),
              Positioned(
                top: h * 0.052,
                left: w * 0.079,
                child: Container(
                  width: circleSize,
                  height: circleSize,
                  decoration: const BoxDecoration(color: Colors.white, shape: BoxShape.circle),
                ),
              ),
              Positioned(
                top: h * 0.129,
                right: w * 0.214,
                child: const _FloatCard(
                  icon: Icons.qr_code_scanner,
                  label: 'Scan & Track',
                  sub: 'Barcode + manual entry',
                ),
              ),
              Positioned(
                top: h * 0.483,
                right: w * 0.053,
                child: const _FloatCard(
                  icon: Icons.inventory_2_outlined,
                  label: '12 items expiring',
                  sub: 'Next 3 days · act now',
                  iconColor: _warn,
                ),
              ),
              Positioned(
                top: h * 0.837,
                right: w * 0.233,
                child: const _FloatCard(
                  icon: Icons.restaurant_menu,
                  label: '8 recipes ready',
                  sub: 'Based on your fridge',
                ),
              ),
            ],
          ),
        );
      },
    );
  }
}

class _FloatCard extends StatelessWidget {
  final IconData icon;
  final String label;
  final String sub;
  final Color iconColor;

  const _FloatCard({
    required this.icon,
    required this.label,
    required this.sub,
    this.iconColor = _dark,
  });

  @override
  Widget build(BuildContext context) => Container(
    padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
    decoration: BoxDecoration(
      color: _card,
      borderRadius: BorderRadius.circular(14),
      boxShadow: [
        BoxShadow(
          color: _dark.withValues(alpha: 0.12),
          blurRadius: 20,
          offset: const Offset(0, 6),
        ),
      ],
    ),
    child: Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 34,
          height: 34,
          decoration: const BoxDecoration(color: _iconBg, shape: BoxShape.circle),
          child: Icon(icon, size: 16, color: iconColor),
        ),
        const SizedBox(width: 10),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              label,
              style: const TextStyle(
                fontSize: 12,
                fontWeight: FontWeight.w700,
                color: _dark,
              ),
            ),
            const SizedBox(height: 2),
            Text(sub, style: const TextStyle(fontSize: 11, color: _muted)),
          ],
        ),
      ],
    ),
  );
}

class _FeaturesSection extends StatelessWidget {
  const _FeaturesSection();

  @override
  Widget build(BuildContext context) {
    final wide = MediaQuery.of(context).size.width > 980;
    return Container(
      color: _bg,
      padding: EdgeInsets.symmetric(horizontal: wide ? 60 : 20, vertical: 60),
      child: Column(
        children: [
          const Text(
            'WHY SMARTFRIDGE?',
            style: TextStyle(
              fontSize: 10,
              fontWeight: FontWeight.w700,
              color: _gold,
              letterSpacing: 1.5,
            ),
          ),
          const SizedBox(height: 10),
          const Text(
            'Everything your kitchen needs',
            style: TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: _dark),
            textAlign: TextAlign.center,
          ),
          const SizedBox(height: 36),
          wide
              ? Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: const [
                  Expanded(
                    child: _FeatureCard(
                      cardBg: _card,
                      iconBg: _iconBg,
                      icon: Icons.inventory_2_outlined,
                      iconColor: _dark,
                      title: 'Inventory Tracking',
                      titleColor: _dark,
                      body: "Know exactly what's in your fridge at all times. Expiry alerts keep you ahead of waste.",
                      bodyColor: _body,
                    ),
                  ),
                  SizedBox(width: 16),
                  Expanded(
                    child: _FeatureCard(
                      cardBg: _brown,
                      iconBg: Colors.white,
                      icon: Icons.menu_book_outlined,
                      iconColor: _brown,
                      title: 'Recipe Suggestions',
                      titleColor: _nav,
                      body: 'Get personalised recipes every day based on what you already have at home.',
                      bodyColor: Color(0xCCFFFCF7),
                    ),
                  ),
                  SizedBox(width: 16),
                  Expanded(
                    child: _FeatureCard(
                      cardBg: _card,
                      iconBg: _iconBg,
                      icon: Icons.notifications_outlined,
                      iconColor: _dark,
                      title: 'Expiry Alerts',
                      titleColor: _dark,
                      body: 'Smart notifications remind you to use ingredients before they go bad.',
                      bodyColor: _body,
                    ),
                  ),
                ],
              )
              : const Column(
                children: [
                  _FeatureCard(
                    cardBg: _card,
                    iconBg: _iconBg,
                    icon: Icons.inventory_2_outlined,
                    iconColor: _dark,
                    title: 'Inventory Tracking',
                    titleColor: _dark,
                    body: "Know exactly what's in your fridge at all times. Expiry alerts keep you ahead of waste.",
                    bodyColor: _body,
                  ),
                  SizedBox(height: 12),
                  _FeatureCard(
                    cardBg: _brown,
                    iconBg: Colors.white,
                    icon: Icons.menu_book_outlined,
                    iconColor: _brown,
                    title: 'Recipe Suggestions',
                    titleColor: _nav,
                    body: 'Get personalised recipes every day based on what you already have at home.',
                    bodyColor: Color(0xCCFFFCF7),
                  ),
                  SizedBox(height: 12),
                  _FeatureCard(
                    cardBg: _card,
                    iconBg: _iconBg,
                    icon: Icons.notifications_outlined,
                    iconColor: _dark,
                    title: 'Expiry Alerts',
                    titleColor: _dark,
                    body: 'Smart notifications remind you to use ingredients before they go bad.',
                    bodyColor: _body,
                  ),
                ],
              ),
        ],
      ),
    );
  }
}

class _FeatureCard extends StatelessWidget {
  final Color cardBg;
  final Color iconBg;
  final IconData icon;
  final Color iconColor;
  final String title;
  final Color titleColor;
  final String body;
  final Color bodyColor;

  const _FeatureCard({
    required this.cardBg,
    required this.iconBg,
    required this.icon,
    required this.iconColor,
    required this.title,
    required this.titleColor,
    required this.body,
    required this.bodyColor,
  });

  @override
  Widget build(BuildContext context) => Container(
    padding: const EdgeInsets.all(22),
    decoration: BoxDecoration(color: cardBg, borderRadius: BorderRadius.circular(16)),
    child: Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          width: 40,
          height: 40,
          decoration: BoxDecoration(color: iconBg, borderRadius: BorderRadius.circular(10)),
          child: Icon(icon, color: iconColor, size: 20),
        ),
        const SizedBox(height: 14),
        Text(
          title,
          style: TextStyle(fontSize: 14, fontWeight: FontWeight.w800, color: titleColor),
        ),
        const SizedBox(height: 8),
        Text(body, style: TextStyle(fontSize: 12, color: bodyColor, height: 1.6)),
      ],
    ),
  );
}

class _StatsBar extends StatelessWidget {
  const _StatsBar();

  static const _stats = [
    ('12,000+', 'ACTIVE HOUSEHOLDS'),
    ('3M+', 'MEALS PLANNED'),
    (r'$340', 'AVG. ANNUAL SAVING'),
    ('68%', 'LESS FOOD WASTE'),
  ];

  @override
  Widget build(BuildContext context) {
    final wide = MediaQuery.of(context).size.width > 980;
    return Container(
      color: _dark,
      padding: EdgeInsets.symmetric(horizontal: wide ? 60 : 24, vertical: 24),
      child: wide
          ? Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              for (final s in _stats) _StatItem(value: s.$1, label: s.$2),
            ],
          )
          : Column(
            children: [
              for (int i = 0; i < _stats.length; i++) ...[
                if (i > 0) const SizedBox(height: 20),
                _StatItem(value: _stats[i].$1, label: _stats[i].$2),
              ],
            ],
          ),
    );
  }
}

class _StatItem extends StatelessWidget {
  final String value;
  final String label;
  const _StatItem({required this.value, required this.label});

  @override
  Widget build(BuildContext context) => Column(
    children: [
      Text(
        value,
        style: const TextStyle(fontSize: 22, fontWeight: FontWeight.w900, color: _gold),
      ),
      const SizedBox(height: 4),
      Text(
        label,
        style: TextStyle(
          fontSize: 10,
          color: _nav.withValues(alpha: 0.7),
          letterSpacing: 1.2,
          fontWeight: FontWeight.w400,
        ),
      ),
    ],
  );
}
