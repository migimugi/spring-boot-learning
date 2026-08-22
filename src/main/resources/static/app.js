const todoList = document.querySelector("#todo-list");
const message = document.querySelector("#message");
const todoForm = document.querySelector("#todo-form");
const todoTitle = document.querySelector("#todo-title");

async function loadTodos() {
    message.textContent = "";

    try {
        const response = await fetch("/todos");

        if (!response.ok) {
            throw new Error("Todoの取得に失敗しました");
        }

        const todos = await response.json();

        todoList.replaceChildren();

        for (const todo of todos) {
            const item = document.createElement("li"); // <li>要素を作る
            const checkbox = document.createElement("input"); // <input>要素を作る
            checkbox.type = "checkbox";
            checkbox.checked = todo.completed; // completedのブール値をチェック状態へ反映

            const title = document.createElement("span");
            title.textContent = todo.title;

            // async: この関数の中には、終わるのを待たないといけない作業があるかも
            checkbox.addEventListener("change", async () => {
                try {
                    const response = await fetch(`/todos/${todo.id}`, {
                        method: "PUT",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            title: todo.title,
                            completed: checkbox.checked
                        })
                    });

                    if (!response.ok) {
                        throw new Error("Todoの更新に失敗しました");
                    }

                    await loadTodos();
                    message.textContent = "Todoを更新しました";
                } catch (error) {
                    checkbox.checked = todo.completed;
                    message.textContent = error.message;
                    console.error(error);
                }
            });

            const deleteButton = document.createElement("button");
            deleteButton.textContent = "削除";

            deleteButton.addEventListener("click", async () => {
                try {
                    const response = await fetch(`/todos/${todo.id}`, {
                        method: "DELETE"
                    });

                    if (!response.ok) {
                        throw new Error("Todoの削除に失敗しました");
                    }

                    await loadTodos();
                    message.textContent = "Todoを削除しました";
                } catch (error) {
                    message.textContent = error.message;
                    console.error(error);
                }
            });

            item.append(checkbox, title, deleteButton);
            todoList.append(item);
        }

    } catch (error) {
        message.textContent = error.message;
        console.error(error);
    }
}

todoForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const title = todoTitle.value.trim();

    if (title === "") {
        message.textContent = "タイトルを入力してください";
        return;
    }

    try {
        const response = await fetch("/todos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                title: title
            })
        });

        if (!response.ok) {
            throw new Error("Todoの追加に失敗しました");
        }

        todoTitle.value = "";

        await loadTodos();
        message.textContent = "Todoを追加しました";
    } catch (error) {
        message.textContent = error.message;
        console.error(error);
    }
});

loadTodos();