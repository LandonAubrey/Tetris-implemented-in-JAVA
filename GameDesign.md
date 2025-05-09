# 游戏设计说明
本游戏是一款经典的俄罗斯方块游戏。玩家使用键盘方向键（上：旋转，下：加速下落，左/右：左右移动）和空格键（快速下落）来控制不同形状的方块。目标是通过填满水平行来消除它们并获得分数。当方块堆叠到游戏区域顶部时，游戏结束。游戏界面主要分为左侧的游戏区域和右侧的信息/控制面板。左侧实时显示下落的方块和已固定的方块，右侧显示当前分数、历史最高分、当前难度等级、操作说明以及控制游戏（调整难度、重新开始、暂停/继续、退出、查看排行榜）的按钮。

# 类设计说明
- Block 类：定义单个俄罗斯方块（Tetromino）。它存储方块的形状（二维数组）、颜色、在游戏区域中的坐标（x, y），并提供旋转（顺时针 rotate 和逆时针 rotateBack 用于撤销无效旋转）和移动的方法。
- GameBoard 类：GameBoard: 管理核心游戏区域逻辑。它包含一个二维数组 board 来表示游戏区域的状态，维护当前下落的方块 currentBlock。负责生成新方块 (spawnNewBlock)，处理方块的移动 (moveLeft, moveRight, moveDown) 和旋转 (rotate)，执行碰撞检测 (canMove) 以确保移动/旋转的有效性，将方块固定到游戏区域 (placeBlock)，检测并清除已填满的行 (clearLines)，判断游戏是否结束 (isGameOver)，以及绘制游戏区域和方块 (draw)。
- ScoreManager 类：负责管理游戏分数。它记录当前分数 score，根据消除的行数增加分数 (addScore)，跟踪并更新历史最高分 highScore，并将最高分持久化存储到文件 (saveHighScore, loadHighScore)。  
- DifficultyManager 类：控制游戏的难度。它定义了不同难度等级对应的方块下落速度 (SPEEDS) 和自动提升难度的分数阈值 (SCORE_THRESHOLDS)。允许根据当前分数自动更新难度 (updateDifficulty)，也支持玩家通过按钮手动调整难度 (increaseDifficulty, decreaseDifficulty)。提供获取当前下落速度 (getCurrentSpeed) 和难度名称 (getCurrentLevel) 的方法。  
- TetrisGame 类：作为游戏的主 JPanel，整合了游戏的核心组件 (GameBoard, ScoreManager, DifficultyManager)。它初始化游戏界面，设置游戏循环的 Timer 来控制方块自动下落和游戏逻辑更新，通过 KeyAdapter 处理玩家的键盘输入，管理游戏状态（进行中、暂停 isPaused、结束 isGameOver），并负责在屏幕上绘制所有游戏元素 (paintComponent)。它还创建并管理右侧面板的控制按钮及其事件监听器。  
- LeaderboardManager 类：管理排行榜数据的存储和读取。它定义了一个内部类 Record 来表示单条记录（分数、难度、时间）。saveRecord 方法将满足条件（分数 > 1000）的游戏结果追加到 leaderboard.txt 文件，loadRecords 方法从该文件读取所有记录。
- LeaderboardDialog 类：一个 JDialog 弹窗，用于显示排行榜。它使用 LeaderboardManager 加载记录，将分数超过 1000 的记录显示在 JTable 中。提供一个 JComboBox 允许用户按难度（包括“全部”）筛选排行榜条目，并按分数降序排列。
- Main 类：应用程序的入口点。负责创建主窗口 JFrame，设置整体布局，实例化 TetrisGame 面板和右侧的控制面板，将分数、难度等信息标签和控制按钮添加到侧面板，并启动界面更新定时器。确保 GUI 操作在事件分发线程（EDT）中执行。

# 特殊功能设计说明
- 难度调整：游戏难度（主要体现为方块下落速度）会根据玩家获得的分数自动提升。此外，游戏界面提供了“增加难度”和“降低难度”按钮，允许玩家在游戏过程中手动实时调整难度级别，以适应不同水平的玩家或追求更高挑战。
- 排行榜：游戏会自动记录得分超过 1000 分的玩家成绩，包括得分、游戏难度和达成时间。通过“排行榜”按钮可以打开一个独立的对话框，展示所有记录。该排行榜支持按“简单”、“中等”、“困难”、“专家”、“大师”等不同难度级别进行筛选查看，方便玩家比较自己在不同难度下的表现。
