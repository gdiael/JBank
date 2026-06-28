#!/bin/bash
# Instala o hook commit-msg no diretório .git/hooks/

HOOK_NAME="commit-msg"
SOURCE="hooks/$HOOK_NAME"
TARGET=".git/hooks/$HOOK_NAME"

if [ ! -f "$SOURCE" ]; then
    echo "ERRO: Script $SOURCE não encontrado. Execute este script da raiz do repositório."
    exit 1
fi

if [ ! -d ".git/hooks" ]; then
    echo "ERRO: Diretório .git/hooks não encontrado. Certifique-se de estar na raiz de um repositório Git."
    exit 1
fi

cp "$SOURCE" "$TARGET"
chmod +x "$TARGET"
echo "Hook $HOOK_NAME instalado com sucesso em .git/hooks/"
