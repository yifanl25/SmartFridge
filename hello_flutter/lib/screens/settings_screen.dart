import 'package:flutter/material.dart';

import '../models/health_goal.dart';
import '../services/fridge_api_service.dart';
import '../theme/smart_fridge_tokens.dart';

/// Health goal from PRD; synced with Spring `PUT /api/preference`.
class SettingsScreen extends StatefulWidget {
  const SettingsScreen({super.key, this.useLiveApi = true});

  final bool useLiveApi;

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  HealthGoal? _current;
  bool _loading = true;
  String? _error;

  @override
  void initState() {
    super.initState();
    if (widget.useLiveApi) {
      _load();
    } else {
      _loading = false;
    }
  }

  Future<void> _load() async {
    setState(() {
      _loading = true;
      _error = null;
    });
    try {
      final g = await FridgeApiService.instance.fetchPreference();
      if (!mounted) return;
      setState(() {
        _current = g ?? HealthGoal.muscleBuilding;
        _loading = false;
      });
    } catch (e) {
      if (!mounted) return;
      setState(() {
        _error = '$e';
        _current = HealthGoal.muscleBuilding;
        _loading = false;
      });
    }
  }

  Future<void> _save(HealthGoal goal) async {
    if (!widget.useLiveApi) {
      setState(() => _current = goal);
      return;
    }
    try {
      await FridgeApiService.instance.savePreference(goal);
      if (!mounted) return;
      setState(() => _current = goal);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Health goal saved.')),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('$e')));
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const ColoredBox(
        color: SfColors.cream,
        child: Center(child: CircularProgressIndicator()),
      );
    }

    final current = _current ?? HealthGoal.muscleBuilding;

    return ColoredBox(
      color: SfColors.cream,
      child: CustomScrollView(
        slivers: [
          SliverPadding(
            padding: const EdgeInsets.fromLTRB(28, 28, 28, 12),
            sliver: SliverToBoxAdapter(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Settings',
                    style: TextStyle(
                      fontSize: 34,
                      fontWeight: FontWeight.w800,
                      color: SfColors.brown,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Health goal drives recipe scoring on the server.',
                    style: TextStyle(
                      fontSize: 15,
                      color: SfColors.brownMuted,
                      height: 1.4,
                    ),
                  ),
                  if (_error != null && widget.useLiveApi) ...[
                    const SizedBox(height: 12),
                    Material(
                      color: Colors.orange.shade50,
                      borderRadius: BorderRadius.circular(12),
                      child: Padding(
                        padding: const EdgeInsets.all(12),
                        child: Text(
                          'Could not read preference. Is `./gradlew bootRun` running?\n$_error',
                          style: TextStyle(color: Colors.orange.shade900, fontSize: 13),
                        ),
                      ),
                    ),
                  ],
                ],
              ),
            ),
          ),
          SliverPadding(
            padding: const EdgeInsets.fromLTRB(20, 8, 20, 40),
            sliver: SliverList(
              delegate: SliverChildListDelegate([
                _GoalTile(
                  title: 'Muscle Building',
                  subtitle: 'High protein alignment',
                  selected: current == HealthGoal.muscleBuilding,
                  onTap: () => _save(HealthGoal.muscleBuilding),
                ),
                const SizedBox(height: 12),
                _GoalTile(
                  title: 'Fat Loss',
                  subtitle: 'Lower calorie alignment',
                  selected: current == HealthGoal.fatLoss,
                  onTap: () => _save(HealthGoal.fatLoss),
                ),
                const SizedBox(height: 12),
                _GoalTile(
                  title: 'Blood Sugar Care',
                  subtitle: 'Blood-sugar friendly alignment',
                  selected: current == HealthGoal.bloodSugarCare,
                  onTap: () => _save(HealthGoal.bloodSugarCare),
                ),
                if (widget.useLiveApi) ...[
                  const SizedBox(height: 24),
                  TextButton.icon(
                    onPressed: _load,
                    icon: const Icon(Icons.refresh),
                    label: const Text('Reload from server'),
                  ),
                ],
              ]),
            ),
          ),
        ],
      ),
    );
  }
}

class _GoalTile extends StatelessWidget {
  const _GoalTile({
    required this.title,
    required this.subtitle,
    required this.selected,
    required this.onTap,
  });

  final String title;
  final String subtitle;
  final bool selected;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Material(
      color: SfColors.whiteCard,
      borderRadius: BorderRadius.circular(SfRadii.card),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(SfRadii.card),
        child: Padding(
          padding: const EdgeInsets.all(18),
          child: Row(
            children: [
              Icon(
                selected ? Icons.check_circle : Icons.circle_outlined,
                color: selected ? SfColors.matchGreen : SfColors.brownMuted,
              ),
              const SizedBox(width: 14),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      title,
                      style: TextStyle(
                        fontWeight: FontWeight.w800,
                        fontSize: 16,
                        color: SfColors.brown,
                      ),
                    ),
                    Text(
                      subtitle,
                      style: TextStyle(
                        fontSize: 13,
                        color: SfColors.brownMuted,
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
