
INSERT INTO user_one (user_name, password, is_account_non_locked, failed_attempts, role) VALUES
                                                                                                    ('username', '$2a$12$V7Lw4YbLSfUryHrhPu.Xl.zzcOYfkui5/.Rqs.yDd9V7o.cfeEQju', true, 0, 'USER'),
                                                                                                    ('username01', '$2a$12$V7Lw4YbLSfUryHrhPu.Xl.zzcOYfkui5/.Rqs.yDd9V7o.cfeEQju', true, 0,  'MODERATOR'),
                                                                                                    ('username02', '$2a$12$V7Lw4YbLSfUryHrhPu.Xl.zzcOYfkui5/.Rqs.yDd9V7o.cfeEQju', true, 0,  'SUPER_ADMIN');


INSERT INTO token ( user_name, token ) VALUES
                                                            ('username', 'abc123'),
                                                            ('username01', 'def456'),
                                                            ('username02', 'ghi789');
