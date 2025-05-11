-- Insert 15 default categories (using INSERT IGNORE to skip duplicates)
INSERT IGNORE INTO categories (name, icon) VALUES
('Home Services', 'home'),
('Health & Wellness', 'heart'),
('Education & Tutoring', 'graduation-cap'),
('Beauty & Personal Care', 'spa'),
('Technology & IT', 'laptop'),
('Business Services', 'briefcase'),
('Automotive', 'car'),
('Pet Services', 'paw'),
('Events & Entertainment', 'music'),
('Sports & Fitness', 'dumbbell'),
('Food & Catering', 'utensils'),
('Cleaning Services', 'broom'),
('Home Maintenance', 'tools'),
('Transportation', 'truck'),
('Professional Services', 'handshake');